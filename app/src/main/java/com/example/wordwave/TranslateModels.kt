package com.example.wordwave

data class GptRequest(
    val modelUri: String,
    val completionOptions: CompletionOptions,
    val messages: List<Message>
)

data class CompletionOptions(
    val stream: Boolean = false,
    val temperature: Double = 0.6,
    val maxTokens: Int = 2000
)

data class Message(
    val role: String,
    val text: String
)

data class GptResponse(
    val result: GptResponseResult? = null
)

data class GptResponseResult(
    val alternatives: List<Alternative>? = null,
    val usage: Usage? = null,
    val modelVersion: String? = null
)

data class Alternative(
    val message: Message,
    val status: String
)

data class Usage(
    val inputTextTokens: String,
    val completionTokens: String,
    val totalTokens: String
)

