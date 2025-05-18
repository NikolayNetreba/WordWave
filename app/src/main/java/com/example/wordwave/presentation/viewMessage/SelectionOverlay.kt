package com.example.wordwave.presentation.viewMessage

import android.content.Context
import android.graphics.*
import android.view.MotionEvent
import android.view.View

class SelectionOverlay(context: Context, private val fullBitmap: Bitmap) : View(context) {
    // Координаты выделения
    private var startX = 0f
    private var startY = 0f
    private var endX = 0f
    private var endY = 0f
    var onSelectionComplete: ((Bitmap) -> Unit)? = null

    private val overlayPaint = Paint().apply {
        color = Color.parseColor("#AA000000") // полупрозрачный чёрный
    }
    private val borderPaint = Paint().apply {
        color = Color.YELLOW
        style = Paint.Style.STROKE
        strokeWidth = 4f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // Рисуем скриншот фоном
        canvas.drawBitmap(fullBitmap, 0f, 0f, null)
        // Рисуем полупрозрачную область за пределами выделения
        if (startX != endX && startY != endY) {
            val left = minOf(startX, endX)
            val top = minOf(startY, endY)
            val right = maxOf(startX, endX)
            val bottom = maxOf(startY, endY)
            // Затеняем экран вне прямоугольника
            canvas.drawRect(0f, 0f, width.toFloat(), top, overlayPaint)
            canvas.drawRect(0f, bottom, width.toFloat(), height.toFloat(), overlayPaint)
            canvas.drawRect(0f, top, left, bottom, overlayPaint)
            canvas.drawRect(right, top, width.toFloat(), bottom, overlayPaint)
            // Рисуем рамку выделения
            canvas.drawRect(left, top, right, bottom, borderPaint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x; startY = event.y
                endX = startX; endY = startY
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                endX = event.x; endY = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                endX = event.x; endY = event.y
                // Создаём Bitmap выделенной области
                val left = minOf(startX, endX).toInt()
                val top = minOf(startY, endY).toInt()
                val width = Math.abs(endX - startX).toInt()
                val height = Math.abs(endY - startY).toInt()
                if (width > 0 && height > 0) {
                    val cropped = Bitmap.createBitmap(fullBitmap, left, top, width, height)
                    onSelectionComplete?.invoke(cropped)
                }
            }
        }
        return true
    }
}
