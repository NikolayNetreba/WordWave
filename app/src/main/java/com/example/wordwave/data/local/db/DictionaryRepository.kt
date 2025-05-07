package com.example.wordwave.data.local.db

import com.example.wordwave.data.local.db.dao.LanguageDao
import com.example.wordwave.data.local.db.dao.UserDao
import com.example.wordwave.data.local.db.dao.WordDao
import com.example.wordwave.data.local.db.entities.Language
import com.example.wordwave.data.local.db.entities.User
import com.example.wordwave.data.local.db.entities.Word

class DictionaryRepository(
    private val userDao: UserDao,
    private val languageDao: LanguageDao,
    private val wordDao: WordDao
) {
    suspend fun upsertUser(user: User) = userDao.upsertUser(user)
    suspend fun upsertLanguage(language: Language) = languageDao.insertLanguage(language)
    suspend fun upsertWord(word: Word) = wordDao.upsertWord(word)

    suspend fun getLanguages(userId: String): List<Language> = languageDao.getLanguagesByUser(userId)
    suspend fun getWords(languageId: Int): List<Word> = wordDao.getWordsByLanguage(languageId)
    suspend fun updateProgress(wordId: Int, progress: Int) = wordDao.updateProgress(wordId, progress)
}
