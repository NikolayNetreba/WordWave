package com.example.wordwave

data class TranslateRequest(
    val q: String,
    val source: String,
    val target: String,
    val format: String = "text"
)

data class TranslateResponse(
    val translatedText: String
)