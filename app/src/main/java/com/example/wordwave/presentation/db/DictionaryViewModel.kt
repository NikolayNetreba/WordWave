package com.example.wordwave.presentation.db

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.wordwave.presentation.db.entities.Language
import com.example.wordwave.presentation.db.entities.User
import com.example.wordwave.presentation.db.entities.Word
import kotlinx.coroutines.launch

class DictionaryViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val repo = DictionaryRepository(db.userDao(), db.languageDao(), db.wordDao())

    var words by mutableStateOf<List<Word>>(emptyList())
        private set

    var languages by mutableStateOf<List<Language>>(emptyList())
        private set

    fun updateWords(languageId: Int) {
        viewModelScope.launch {
            words = repo.getWords(languageId)
        }
    }

    private fun updateLanguages(userId: String) {
        viewModelScope.launch {
            languages = repo.getLanguages(userId)
        }
    }

    fun updateAll(userId: String) {
        updateLanguages(userId)

        for (language in languages) {
            updateWords(language.id)
        }
    }

    fun addWord(word: String, translation: String) {
        viewModelScope.launch {
            repo.upsertWord(
                Word(
                    languageId = 1,
                    word = word,
                    translation = translation,
                    example = "null",
                    imageUrl = null,
                    progress = 0
                )
            )

            updateWords(1)
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
            //updateLanguages("u0")
            updateWords(langs[0].id)
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
