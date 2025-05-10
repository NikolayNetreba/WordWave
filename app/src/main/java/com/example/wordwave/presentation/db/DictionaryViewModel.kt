package com.example.wordwave.presentation.db

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordwave.presentation.db.entities.Language
import com.example.wordwave.presentation.db.entities.Translation
import com.example.wordwave.presentation.db.entities.User
import com.example.wordwave.presentation.db.entities.Word
import kotlinx.coroutines.launch

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {
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
