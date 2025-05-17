package com.example.wordwave.data.translate

import com.example.wordwave.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*
import io.ktor.client.engine.okhttp.*

class YandexGptService {

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson()
        }
        install(Logging) {
            level = LogLevel.BODY
        }
    }

    suspend fun generateExamples(word: String, from: String, to: String): LibreTranslateApi<List<String>> {
        val apiKey = BuildConfig.API_KEY
        val folderId = BuildConfig.FOLDER_ID

        return try {
            val prompt = """
                Ты профессиональный писатель-переводчик.
                Придумай 5 коротких примеров использования слова "$word" в $from предложениях с переводом на $to.
                Формат ответа строго:
                1. Пример на $from — Перевод на $to.
                2. ...
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

            val rawText = response.result?.alternatives?.firstOrNull()?.message?.text
                ?: return LibreTranslateApi.Failure(Exception("Пустой ответ от GPT"))

            val examples = rawText.lines()
                .map { it.trim() }
                .filter { it.matches(Regex("""\d+\..+—.+""")) }

            LibreTranslateApi.Success(examples)

        } catch (e: Exception) {
            LibreTranslateApi.Failure(e)
        }
    }
}
