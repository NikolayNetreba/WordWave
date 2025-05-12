package com.example.wordwave.presentation

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordwave.data.local.db.entities.Language
import com.example.wordwave.data.local.db.entities.User
import com.example.wordwave.data.local.db.entities.Word
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import com.example.wordwave.data.translate.LibreTranslateApi
import com.example.wordwave.data.translate.YandexGptService
import com.example.wordwave.data.local.db.AppDatabase
import com.example.wordwave.data.local.db.DictionaryRepository
import com.example.wordwave.data.translate.Definition
import com.example.wordwave.data.translate.YandexDictionaryService
import com.example.wordwave.data.translate.YandexTranslateService
import com.example.wordwave.data.translate.onFailure
import com.example.wordwave.data.translate.onSuccess
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
    val definitions = MutableStateFlow<List<Definition>>(emptyList())

    // Состояния UI
    val primaryTranslation = MutableStateFlow("")
    val additionalTranslations = MutableStateFlow<List<String>>(emptyList())
    val synonyms = MutableStateFlow<List<String>>(emptyList())
    val examples = MutableStateFlow<List<String>>(emptyList())

    private val _inputText = MutableStateFlow("")
    val inputText = _inputText.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage = _errorMessage.asStateFlow()

    // Сервисы
    private val translateService = YandexTranslateService()
    private val dictionaryService = YandexDictionaryService()
    private val gptService = YandexGptService()

    fun updateInputText(text: String) {
        _inputText.value = text
    }

    fun clearInputText() {
        _inputText.value = ""
        primaryTranslation.value = ""
        additionalTranslations.value = emptyList()
        synonyms.value = emptyList()
        examples.value = emptyList()
        _errorMessage.value = null
        definitions.value = emptyList()
    }

    fun translateWord(fromLang: String = "en", toLang: String = "ru") {
        val word = _inputText.value.trim()
        if (word.isEmpty()) return

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                // 1. Перевод
                when (val response = translateService.translate(word, fromLang, toLang)) {
                    is LibreTranslateApi.Success<*> -> primaryTranslation.value = response.data.toString()
                    is LibreTranslateApi.Failure -> _errorMessage.value = "Ошибка перевода: ${response.exception.localizedMessage}"
                }

                // 2. Словарь
                dictionaryService.lookup(word).onSuccess { response ->
                    val defs = response.def

                    definitions.value = defs

                    val allTranslations = mutableSetOf<String>()
                    val allSynonyms = mutableSetOf<String>()

                    for (def in defs) {
                        for (tr in def.tr) {
                            allTranslations.add(tr.text)
                            tr.syn?.let { syns ->
                                syns.forEach { allSynonyms.add(it.text) }
                            }
                        }
                    }

                    additionalTranslations.value = allTranslations.toList()
                    synonyms.value = allSynonyms.toList()
                }.onFailure {
                    _errorMessage.value = "Ошибка словаря: ${it.localizedMessage}"
                }

                // 3. Примеры
                when (val gptResponse = gptService.generateExamples(word)) {
                    is LibreTranslateApi.Success -> examples.value = gptResponse.data
                    is LibreTranslateApi.Failure -> _errorMessage.value = "Ошибка GPT: ${gptResponse.exception.localizedMessage}"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Непредвиденная ошибка: ${e.localizedMessage}"
            }

            _isLoading.value = false
        }
    }
}


