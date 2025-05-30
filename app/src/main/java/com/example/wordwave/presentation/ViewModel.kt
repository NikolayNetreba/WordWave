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
import androidx.navigation.NavController
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlin.math.min
import kotlin.random.Random

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repo = DictionaryRepository(db.userDao(), db.languageDao(), db.dictionaryDao())

    var words by mutableStateOf<List<Word>>(emptyList())
        private set

    var wordsWithTranslations by mutableStateOf<List<WordWithTranslations>>(emptyList())
        private set

    var languages by mutableStateOf<List<Language>>(emptyList())
        private set

    var currentDefinitions: List<Definition>? = null
    var currentWord: String? = null
    var currentImagePath: String? = null

    var cr: WordWithTranslations? = null

    fun saveDefinitions() {
        if (currentDefinitions == null || currentWord == null || currentDefinitions!!.isEmpty()) {
            return
        }

        val map = HashMap<String, List<String>>()
        for (df in currentDefinitions!!) {
            for (tr in df.tr) {
                if (tr.syn == null) {
                    map[tr.text] = emptyList()
                    continue
                }

                var synonyms = emptyList<String>()
                for (sn in tr.syn) {
                    synonyms += sn.text;
                }

                map[tr.text] = synonyms
            }
        }

        addWordWithTranslations(currentWord!!, map, currentImagePath)
        updateWordsWithTranslations("хуй")
    }

    fun initialize() {
        viewModelScope.launch(Dispatchers.IO) {
            if (repo.getUserById("u0") == null) {
                repo.upsertUser(User("u0"))
            }

            if (repo.getLanguages("u0").isEmpty()) {
                repo.upsertLanguage(Language(userId = "u0", name = "English", code = "en"))
            }

            updateLanguages("u0")
            updateWordsWithTranslations("en")
        }
    }

    fun getWordWithTranslation(word: String): WordWithTranslations? {
        updateWordsWithTranslations("en")
        for (w in wordsWithTranslations) {
            if (w.word.text == word) {
                return w
            }
        }

        return null
    }

    fun updateWords(languageId: Int) {
        /*viewModelScope.launch {
            words = repo.getWords(languageId)
        }*/
    }

    fun updateWordsWithTranslations(language: String) {
        viewModelScope.launch(Dispatchers.IO) {
            wordsWithTranslations = repo.getWords()
        }
    }

    fun updateLanguages(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
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

    fun addWordWithTranslations(word: String, translations: Map<String, List<String>>, imagePath: String?) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.upsertWordWithTranslations(
                Word(
                    text = word,
                    language = "en",
                    imagePath = imagePath
                ),
                translations
            )

            updateWordsWithTranslations("en")
        }
    }

    fun addWordWithTranslation(word: String, translation: String) {
        /*viewModelScope.launch {
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
        }*/
    }

    fun addSampleData() {
        viewModelScope.launch(Dispatchers.IO) {
            val user = User("u0")
            repo.upsertUser(user)

            val lang = Language(userId = "u0", name = "English", code = "en")
            repo.upsertLanguage(lang)
        }
    }

    fun updateProgress(wordId: Int, newProgress: Int) {
        /*viewModelScope.launch {
            repo.updateProgress(wordId, newProgress)
            val currentLangId = words.firstOrNull()?.languageId ?: return@launch
            updateWords(currentLangId)
        }*/
    }

    fun deleteWord(word: Word) {
        viewModelScope.launch(Dispatchers.IO) {
            repo.deleteWord(word)
            // обновляем список после удаления
            updateWordsWithTranslations("en")
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
                    is LibreTranslateApi.Failure -> _errorMessage.value =
                        "Ошибка перевода: ${response.exception.localizedMessage}"
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
                    is LibreTranslateApi.Failure -> _errorMessage.value =
                        "Ошибка GPT: ${gptResponse.exception.localizedMessage}"
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

const val WORDS_NUM = 10

class FlashCardsViewModel() : ViewModel() {
    lateinit var navController: NavController
    lateinit var dictionaryViewModel: DictionaryViewModel

    var wordsNum = 0

    private lateinit var words: List<Pair<String, String>>

    private val _currentIndex = mutableStateOf(0)
    private val _isFlipped = mutableStateOf(false)

    val currentWord: Pair<String, String>?
        get() = if (words.isEmpty()) null else words[_currentIndex.value]

    val cardIndex: State<Int> = _currentIndex
    val isFlipped: State<Boolean> = _isFlipped

    fun initialize() {
        words = emptyList()
        dictionaryViewModel.updateWordsWithTranslations("en")
        wordsNum = min(dictionaryViewModel.wordsWithTranslations.size, WORDS_NUM)
        val wordsWithTranslations = dictionaryViewModel.wordsWithTranslations.shuffled().take(wordsNum)

        for (i in 0..wordsNum - 1) {
            words += Pair(
                wordsWithTranslations[i].word.text,
                wordsWithTranslations[i].translations[Random.nextInt(
                    0,
                    wordsWithTranslations[i].translations.size
                )].translation.translatedText
            )
        }
    }

    fun getAllTranslationsForCurrentWord(): List<String> {
        var list: List<String> = emptyList()
        for (wwt in dictionaryViewModel.wordsWithTranslations) {
            if (wwt.word.text != currentWord?.first) {
                continue;
            }

            for (translation in wwt.translations) {
                list += translation.translation.translatedText;
            }
        }

        return list
    }

    fun flipCard() {
        _isFlipped.value = !_isFlipped.value
    }

    fun onRememberClicked() {
        flipCard()
        nextCard()
    }

    fun onDontRememberClicked() {
        flipCard()
        nextCard()
    }

    fun nextCard() {
        // Сбрасываем переворот
        _isFlipped.value = false
        // Переходим к следующей
        if (_currentIndex.value < words.lastIndex) {
            _currentIndex.value += 1
        } else {
            navController.popBackStack();
            _currentIndex.value = 0
        }
    }
}

class FakeViewModel : ViewModel() {
    fun addSampleData() {

    }
}