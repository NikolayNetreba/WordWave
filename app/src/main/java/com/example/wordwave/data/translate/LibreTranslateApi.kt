package com.example.wordwave.data.translate

sealed class LibreTranslateApi<out T> {
    data class Success<out T>(val data: T) : LibreTranslateApi<T>()
    data class Failure(val exception: Exception) : LibreTranslateApi<Nothing>()
}

fun <T> LibreTranslateApi<T>.onSuccess(action: (T) -> Unit): LibreTranslateApi<T> {
    if (this is LibreTranslateApi.Success) action(data)
    return this
}

fun <T> LibreTranslateApi<T>.onFailure(action: (Exception) -> Unit): LibreTranslateApi<T> {
    if (this is LibreTranslateApi.Failure) action(exception)
    return this
}