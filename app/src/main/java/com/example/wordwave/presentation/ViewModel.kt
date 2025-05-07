package com.example.wordwave.presentation

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordwave.data.local.dp.entities.Language
import com.example.wordwave.data.local.dp.entities.User
import com.example.wordwave.data.local.dp.entities.Word
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.wordwave.data.translate.LibreTranslateApi
import com.example.wordwave.data.translate.YandexGptService
import com.example.wordwave.data.local.dp.AppDatabase
import com.example.wordwave.data.local.dp.DictionaryRepository
import kotlinx.coroutines.flow.*

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.Companion.getDatabase(application)
    private val repo = DictionaryRepository(db.userDao(), db.languageDao(), db.wordDao())

    var words by mutableStateOf<List<Word>>(emptyList())
        private set

    fun loadWords(languageId: Int) {
        viewModelScope.launch {
            words = repo.getWords(languageId)
        }
    }

    fun addSampleData() {
        viewModelScope.launch {
            val user = User("u0")
            repo.upsertUser(user)

            val lang = Language(userId = "u0", name = "English", code = "en")
            repo.upsertLanguage(lang)

            val langs = repo.getLanguages("u0")
            val word = Word(
                languageId = langs[0].id,
                word = "apple",
                translation = "яблоко",
                example = "I ate an apple.",
                imageUrl = null,
                progress = 0
            )

            repo.upsertWord(word)
            loadWords(langs[0].id)
        }
    }

    fun updateProgress(wordId: Int, newProgress: Int) {
        viewModelScope.launch {
            repo.updateProgress(wordId, newProgress)
            // Reload to reflect changes
            val currentLangId = words.firstOrNull()?.languageId ?: return@launch
            loadWords(currentLangId)
        }
    }
}

class TranslationViewModel : ViewModel() {
    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    private val _translatedText = MutableStateFlow("")
    val translatedText = _translatedText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    private val yandexGptService = YandexGptService()

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun clearInputText(){
        _inputText.value = ""
    }

    fun translateText() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val response = yandexGptService.translateText(_inputText.value)

            when (response) {
                is LibreTranslateApi.Success -> _translatedText.value = response.data
                is LibreTranslateApi.Failure -> _errorMessage.value = "Ошибка: ${response.exception.localizedMessage}"
            }

            _isLoading.value = false
        }
    }
}
