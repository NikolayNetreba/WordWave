package com.example.wordwave.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordwave.data.local.db.entities.Language
import com.example.wordwave.data.local.db.entities.User
import com.example.wordwave.data.local.db.entities.Word
import kotlinx.coroutines.launch
import androidx.lifecycle.ViewModel
import androidx.compose.runtime.*
import com.example.wordwave.data.translate.LibreTranslateApi
import com.example.wordwave.data.translate.YandexGptService
import com.example.wordwave.data.local.db.AppDatabase
import com.example.wordwave.data.local.db.DictionaryRepository
import com.example.wordwave.data.local.db.WordWithTranslations
import com.example.wordwave.data.translate.Definition
import com.example.wordwave.data.translate.YandexDictionaryService
import com.example.wordwave.data.translate.YandexTranslateService
import com.example.wordwave.data.translate.onFailure
import com.example.wordwave.data.translate.onSuccess
import kotlinx.coroutines.flow.*

class ViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repo = DictionaryRepository(db.userDao(), db.languageDao(), db.dictionaryDao())

    var words by mutableStateOf<List<Word>>(emptyList())
        private set

    var wordsWithTranslations by mutableStateOf<List<WordWithTranslations>>(emptyList())
        private set

    var languages by mutableStateOf<List<Language>>(emptyList())
        private set

    fun updateWords(languageId: Int) {
        viewModelScope.launch {
            words = repo.getWords(languageId)
        }
    }

    fun updateWordsWithTranslations(languageId: Int) {
        viewModelScope.launch {
            wordsWithTranslations = repo.getWordsWithTranslations(languageId)
        }
    }

    fun updateLanguages(userId: String) {
        viewModelScope.launch {
            languages = repo.getLanguages(userId)
        }
    }

    fun updateAll(userId: String) {
        viewModelScope.launch {
            updateLanguages(userId)

            for (language in languages) {
                updateWords(language.id)
            }
        }
    }

    fun addWordWithTranslations(word: String, translations: List<String>) {
        viewModelScope.launch {
            repo.upsertWordWithTranslations(
                Word(
                    languageId = 1,
                    word = word,
                    example = "null",
                    imageUrl = null,
                    progress = 0
                ),
                translations
            )

            updateWordsWithTranslations(1)
        }
    }

    fun addWordWithTranslation(word: String, translation: String) {
        viewModelScope.launch {
            repo.upsertWordWithTranslations(
                Word(
                    languageId = 1,
                    word = word,
                    example = "null",
                    imageUrl = null,
                    progress = 0
                ),
                listOf(translation)
            )

            updateWordsWithTranslations(1)
        }
    }

    fun addSampleData() {
        viewModelScope.launch {
            val user = User("u0")
            repo.upsertUser(user)

            val lang = Language(userId = "u0", name = "English", code = "en")
            repo.upsertLanguage(lang)
        }
    }

    fun updateProgress(wordId: Int, newProgress: Int) {
        viewModelScope.launch {
            repo.updateProgress(wordId, newProgress)
            val currentLangId = words.firstOrNull()?.languageId ?: return@launch
            updateWords(currentLangId)
        }
    }
}


class TranslationViewModel : ViewModel() {
    val definitions = MutableStateFlow<List<Definition>>(emptyList())

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

    private val translateService = YandexTranslateService()
    private val dictionaryService = YandexDictionaryService()
    private val gptService = YandexGptService()

    private val _fromLang = mutableStateOf("en")
    val fromLang: State<String> = _fromLang

    private val _toLang = mutableStateOf("ru")
    val toLang: State<String> = _toLang

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

    fun translateWord() {
        val word = _inputText.value.trim()
        if (word.isEmpty()) return

        val from = fromLang.value
        val to = toLang.value

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            try {
                when (val response = translateService.translate(word, from, to)) {
                    is LibreTranslateApi.Success<*> -> primaryTranslation.value = response.data.toString()
                    is LibreTranslateApi.Failure -> _errorMessage.value = "Ошибка перевода: ${response.exception.localizedMessage}"
                }

                dictionaryService.lookup(word, from, to).onSuccess { response ->
                    val defs = response.def
                    definitions.value = defs
                    val allTranslations = mutableSetOf<String>()
                    val allSynonyms = mutableSetOf<String>()
                    for (def in defs) {
                        for (tr in def.tr) {
                            allTranslations.add(tr.text)
                            tr.syn?.forEach { allSynonyms.add(it.text) }
                        }
                    }
                    additionalTranslations.value = allTranslations.toList()
                    synonyms.value = allSynonyms.toList()
                }.onFailure {
                    _errorMessage.value = "Ошибка словаря: ${it.localizedMessage}"
                }

                when (val gptResponse = gptService.generateExamples(word, from, to)) {
                    is LibreTranslateApi.Success -> examples.value = gptResponse.data
                    is LibreTranslateApi.Failure -> _errorMessage.value = "Ошибка GPT: ${gptResponse.exception.localizedMessage}"
                }

            } catch (e: Exception) {
                _errorMessage.value = "Непредвиденная ошибка: ${e.localizedMessage}"
            }

            _isLoading.value = false
        }
    }

    fun setLanguagesSmart(selectedLang: String, isSource: Boolean) {
        val from = _fromLang.value
        val to = _toLang.value
        val input = _inputText.value
        val output = primaryTranslation.value

        if ((isSource && selectedLang == to) || (!isSource && selectedLang == from)) {
            _fromLang.value = to
            _toLang.value = from
            _inputText.value = output
            primaryTranslation.value = input
            translateWord()
            return
        }

        if (isSource) {
            _fromLang.value = selectedLang
            translateWord()
        } else {
            _toLang.value = selectedLang
            translateWord()
        }
    }

    fun swapLanguages() {
        val from = _fromLang.value
        val to = _toLang.value
        val input = _inputText.value
        val output = primaryTranslation.value

        _fromLang.value = to
        _toLang.value = from
        _inputText.value = output
        primaryTranslation.value = input
        translateWord()
    }
}

data class TWord(
    val word: String,
    val translation: String
)

class FlashCardsViewModel : ViewModel() {
    private val _words = listOf(
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука"),
        TWord("hello", "сука")
    ).shuffled().take(10)

    private val _currentIndex = mutableStateOf(0)
    private val _isFlipped = mutableStateOf(false)

    val currentWord: TWord
        get() = _words[_currentIndex.value]

    val cardIndex: State<Int> = _currentIndex
    val isFlipped: State<Boolean> = _isFlipped

    fun flipCard() {
        _isFlipped.value = !_isFlipped.value
    }

    fun onRememberClicked() {

    }

    fun onDontRememberClicked() {

    }

    fun nextCard() {
        // Сбрасываем переворот
        _isFlipped.value = false
        // Переходим к следующей
        if (_currentIndex.value < _words.lastIndex) {
            _currentIndex.value += 1
        } else {
            // Последняя карточка — можно обработать окончание игры
        }
    }
}

class FakeViewModel : ViewModel() {
    fun addSampleData() {

    }
}