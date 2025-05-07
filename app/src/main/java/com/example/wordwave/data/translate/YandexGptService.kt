package com.example.wordwave.data.translate

import com.example.wordwave.BuildConfig
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
        text: String
    ): LibreTranslateApi<String> {
        val apiKey = BuildConfig.API_KEY
        val folderId = BuildConfig.FOLDER_ID

        return try {
            val prompt = """
                Ты профессиональный переводчик. Переведи слово или фразу "$text" с английского на русский язык.
                Дай только основной перевод.
            """.trimIndent()

            val request = GptRequest(
                modelUri = "gpt://$folderId/yandexgpt-lite",
                completionOptions = CompletionOptions(),
                messages = listOf(
                    Message(role = "system", text = "You are a yandex translator"),
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