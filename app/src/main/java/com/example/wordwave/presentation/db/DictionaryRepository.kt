package com.example.wordwave.presentation.db

import com.example.wordwave.presentation.db.dao.DictionaryDao
import com.example.wordwave.presentation.db.dao.LanguageDao
import com.example.wordwave.presentation.db.dao.UserDao
import com.example.wordwave.presentation.db.dao.WordDao
import com.example.wordwave.presentation.db.entities.Language
import com.example.wordwave.presentation.db.entities.Translation
import com.example.wordwave.presentation.db.entities.User
import com.example.wordwave.presentation.db.entities.Word

class DictionaryRepository(
    private val userDao: UserDao,
    private val languageDao: LanguageDao,
    private val dictionaryDao: DictionaryDao
) {
    suspend fun upsertUser(user: User) = userDao.upsertUser(user)
    suspend fun upsertLanguage(language: Language) = languageDao.upsertLanguage(language)
    suspend fun upsertWord(word: Word) = dictionaryDao.upsertWord(word)
    suspend fun upsertWordWithTranslations(word: Word, translations: List<String>) =
        dictionaryDao.upsertWordWithTranslations(word, translations)

    suspend fun getLanguages(userId: String): List<Language> = languageDao.getLanguagesByUser(userId)
    suspend fun getWords(languageId: Int): List<Word> = dictionaryDao.getWordsByLanguage(languageId)
    suspend fun getWordsWithTranslations(languageId: Int): List<WordWithTranslations> =
        dictionaryDao.getWordsWithTranslationsByLanguage(languageId)

    suspend fun updateProgress(wordId: Int, progress: Int) = dictionaryDao.updateProgress(wordId, progress)
}
