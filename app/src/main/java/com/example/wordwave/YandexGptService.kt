package com.example.wordwave

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

class YandexGptService {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            gson()
        }
        install(Logging) {
            level = LogLevel.HEADERS
        }
    }

    suspend fun translateText(
        apiKey: String,
        folderId: String,
        text: String
    ): LibreTranslateApi<String> {
        return try {
            val prompt = """
                Ты профессиональный переводчик. Переведи слово или фразу "$text" с английского на русский язык.
                Дай основной перевод, затем дополнительные варианты.
                Если слово может быть разными частями речи, предоставь перевод для каждой.
                Также сгенерируй 5 небольших предложений с этим словом на английском и их перевод на русский.
                
                Формат вывода:
                Основной перевод: [перевод]
                
                Дополнительные переводы:
                - [часть речи]: [перевод]
                
                Примеры:
                1. [английское предложение] - [русский перевод]
                2. ...
            """.trimIndent()

            val request = GptRequest(
                modelUri = "gpt://$folderId/yandexgpt-lite",
                completionOptions = CompletionOptions(),
                messages = listOf(
                    Message(role = "system", text = "You are a yandex translator, and you "),
                    Message(role = "user", text = prompt)
                )
            )

            val response: GptResponse = client.post("https://llm.api.cloud.yandex.net/foundationModels/v1/completion") {
                header(HttpHeaders.Authorization, "Api-Key $apiKey")
                header("x-folder-id", folderId)
                contentType(ContentType.Application.Json)
                setBody(request)
            }.body()

            val translation = response.result?.alternatives?.firstOrNull()?.message?.text
                ?: "Не удалось получить перевод"

            LibreTranslateApi.Success(translation)
        } catch (e: Exception) {
            LibreTranslateApi.Failure(e)
        }
    }
}