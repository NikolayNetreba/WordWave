package com.example.wordwave.data.translate

import com.example.wordwave.BuildConfig
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class YandexTranslateService {

    private val apiKey = BuildConfig.YANDEX_TRANSLATE_API_KEY
    private val folderId = BuildConfig.FOLDER_ID

    private val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                prettyPrint = false
                isLenient = true
            })
        }
    }

    suspend fun translate(text: String, sourceLang: String, targetLang: String): LibreTranslateApi<String> {
        return try {
            val response = client.post("https://translate.api.cloud.yandex.net/translate/v2/translate") {
                headers {
                    append(HttpHeaders.Authorization, "Api-Key $apiKey")
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                }
                setBody(
                    YandexTranslateRequest(
                        folderId = folderId,
                        texts = listOf(text),
                        sourceLanguageCode = sourceLang,
                        targetLanguageCode = targetLang
                    )
                )
            }

            if (!response.status.isSuccess()) {
                val errorBody = response.bodyAsText()
                return LibreTranslateApi.Failure(Exception("Ошибка API: ${response.status.value} - $errorBody"))
            }

            val result: YandexTranslateResponse = response.body()

            val translatedText = result.translations.firstOrNull()?.text
            if (translatedText != null) {
                LibreTranslateApi.Success(translatedText)
            } else {
                LibreTranslateApi.Failure(Exception("Ответ без перевода"))
            }

        } catch (e: Exception) {
            LibreTranslateApi.Failure(e)
        }
    }

}

@Serializable
data class YandexTranslateRequest(
    @SerialName("folderId") val folderId: String,
    @SerialName("texts") val texts: List<String>,
    @SerialName("sourceLanguageCode") val sourceLanguageCode: String,
    @SerialName("targetLanguageCode") val targetLanguageCode: String
)

@Serializable
data class YandexTranslateResponse(
    @SerialName("translations") val translations: List<TranslateResult>
)

@Serializable
data class TranslateResult(
    @SerialName("text") val text: String
)
