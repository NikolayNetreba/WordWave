package com.example.wordwave

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class TranslationViewModel : ViewModel() {
    private val _inputText = MutableStateFlow("")
    val inputText: StateFlow<String> = _inputText

    private val _translatedText = MutableStateFlow("")
    val translatedText: StateFlow<String> = _translatedText

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val yandexGptService = YandexGptService()
    private val apiKey = ""
    private val folderId = ""

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun translateText() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val response = yandexGptService.translateText(apiKey, folderId, _inputText.value)

            when (response) {
                is LibreTranslateApi.Success -> _translatedText.value = response.data
                is LibreTranslateApi.Failure -> _errorMessage.value = "Ошибка: ${response.exception.localizedMessage}"
            }

            _isLoading.value = false
        }
    }
}
