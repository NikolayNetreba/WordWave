package com.example.wordwave.data.local.db

import com.example.wordwave.data.local.db.dao.DictionaryDao
import com.example.wordwave.data.local.db.dao.LanguageDao
import com.example.wordwave.data.local.db.dao.UserDao
import com.example.wordwave.data.local.db.entities.Language
import com.example.wordwave.data.local.db.entities.User
import com.example.wordwave.data.local.db.entities.Word

class DictionaryRepository(
    private val userDao: UserDao,
    private val languageDao: LanguageDao,
    private val dictionaryDao: DictionaryDao
) {
    suspend fun upsertUser(user: User) = userDao.upsertUser(user)
    suspend fun upsertLanguage(language: Language) = languageDao.upsertLanguage(language)
    suspend fun upsertWordWithTranslations(word: Word, translations: Map<String, List<String>>) =
        dictionaryDao.upsertWordWithTranslations(word, translations)

    suspend fun getUserById(id: String) = userDao.getUserById(id)
    suspend fun getLanguagesByUserId(id: String) = languageDao.getLanguagesByUser(id)

    suspend fun getLanguages(userId: String): List<Language> = languageDao.getLanguagesByUser(userId)
    suspend fun getWords(): List<WordWithTranslations> = dictionaryDao.getAllWordsWithTranslations()
    /*suspend fun getWordsWithTranslations(language: String): List<WordWithTranslations> =
        dictionaryDao.getWordWithTranslations(language)*/
    suspend fun deleteWord(word: Word) {
        dictionaryDao.deleteWord(word)
    }
}