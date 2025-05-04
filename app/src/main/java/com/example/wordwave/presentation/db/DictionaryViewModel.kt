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
