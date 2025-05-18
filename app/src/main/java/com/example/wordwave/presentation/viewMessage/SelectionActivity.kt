package com.example.wordwave.presentation.viewMessage

import SelectionOverlayView
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.graphics.Rect
import android.graphics.RectF
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.Image
import android.media.ImageReader
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.example.wordwave.data.local.db.OCRHelper
import androidx.core.graphics.createBitmap


class SelectionActivity : ComponentActivity() {

    private lateinit var mediaProjectionManager: MediaProjectionManager
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var imageReader: ImageReader? = null

    private var screenDensity = 0
    private var screenWidth = 0
    private var screenHeight = 0

    private val REQUEST_CODE_SCREEN_CAPTURE = 1001

    private lateinit var overlayView: SelectionOverlayView

    private var pendingSelection: RectF? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val overlayView = SelectionOverlayView(this)
        setContentView(overlayView)
        val metrics = Resources.getSystem().displayMetrics
        screenDensity = metrics.densityDpi
        screenWidth = metrics.widthPixels
        screenHeight = metrics.heightPixels

        mediaProjectionManager = getSystemService(MEDIA_PROJECTION_SERVICE) as MediaProjectionManager

        // Запрос разрешения на захват экрана
        startActivityForResult(mediaProjectionManager.createScreenCaptureIntent(), REQUEST_CODE_SCREEN_CAPTURE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_SCREEN_CAPTURE) {
            if (resultCode == RESULT_OK && data != null) {
                mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, data)
            } else {
                Toast.makeText(this, "Разрешение на захват экрана не получено", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    fun onAreaSelected(selection: RectF?) {
        if (selection == null || mediaProjection == null) {
            Toast.makeText(this, "Ошибка выделения или MediaProjection", Toast.LENGTH_SHORT).show()
            return
        }

        pendingSelection = selection
        captureScreen()
    }

    private fun captureScreen() {
        if (mediaProjection == null) return

        imageReader = ImageReader.newInstance(screenWidth, screenHeight, PixelFormat.RGBA_8888, 2)
        imageReader?.setOnImageAvailableListener({ reader ->
            val image = reader.acquireLatestImage()
            if (image != null) {
                val bitmap = imageToBitmap(image)
                image.close()

                val cropRect = pendingSelection?.let {
                    Rect(
                        it.left.toInt().coerceAtLeast(0),
                        it.top.toInt().coerceAtLeast(0),
                        it.right.toInt().coerceAtMost(bitmap.width),
                        it.bottom.toInt().coerceAtMost(bitmap.height)
                    )
                }

                if (cropRect != null && !cropRect.isEmpty) {
                    val croppedBitmap = Bitmap.createBitmap(bitmap, cropRect.left, cropRect.top, cropRect.width(), cropRect.height())
                    OCRHelper.recognizeText(this, croppedBitmap)
                } else {
                    Toast.makeText(this, "Некорректная область выделения", Toast.LENGTH_SHORT).show()
                }

                stopCapture()
                finish()
            }
        }, Handler(Looper.getMainLooper()))

        virtualDisplay = mediaProjection?.createVirtualDisplay(
            "ScreenCapture",
            screenWidth,
            screenHeight,
            screenDensity,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            imageReader?.surface,
            null,
            null
        )
    }

    private fun imageToBitmap(image: Image): Bitmap {
        val width = image.width
        val height = image.height
        val plane = image.planes[0]
        val buffer = plane.buffer
        val pixelStride = plane.pixelStride
        val rowStride = plane.rowStride
        val rowPadding = rowStride - pixelStride * width

        val bitmap = createBitmap(width + rowPadding / pixelStride, height)
        bitmap.copyPixelsFromBuffer(buffer)
        return bitmap
    }

    private fun stopCapture() {
        virtualDisplay?.release()
        virtualDisplay = null
        imageReader?.setOnImageAvailableListener(null, null)
        imageReader?.close()
        imageReader = null
        mediaProjection?.stop()
        mediaProjection = null
    }

    override fun onDestroy() {
        super.onDestroy()
        stopCapture()
    }
}

