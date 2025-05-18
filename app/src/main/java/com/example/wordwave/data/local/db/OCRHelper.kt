package com.example.wordwave.data.local.db

import android.content.Context
import android.graphics.Bitmap
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object OCRHelper {
    fun recognizeText(context: Context, bitmap: Bitmap) {
        val image = InputImage.fromBitmap(bitmap, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val text = visionText.text
                // Сохраняем распознанный текст во временный кэш
                CoroutineScope(Dispatchers.IO).launch {
                    OCRCache.saveText(context, text)
                }
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }
}
