package com.example.wordwave.data.translate

import com.example.wordwave.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.gson.*

class YandexDictionaryService {

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            gson()
        }
    }

    suspend fun lookup(
        word: String,
        lang: String = "en-ru"
    ): LibreTranslateApi<DictionaryResponse> {
        val apiKey = BuildConfig.YANDEX_DICTIONARY_API_KEY
        return try {
            val response: DictionaryResponse =
                client.get("https://dictionary.yandex.net/api/v1/dicservice.json/lookup") {
                    parameter("key", apiKey)
                    parameter("lang", lang)
                    parameter("text", word)
                }.body()

            LibreTranslateApi.Success(response)
        } catch (e: Exception) {
            LibreTranslateApi.Failure(e)
        }
    }
}
