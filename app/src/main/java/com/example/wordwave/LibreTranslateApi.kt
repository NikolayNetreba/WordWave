package com.example.wordwave

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface LibreTranslateApi {
    @Headers("Content-Type: application/json")
    @POST("translate")
    fun translate(@Body request: TranslateRequest): Call<TranslateResponse>
}