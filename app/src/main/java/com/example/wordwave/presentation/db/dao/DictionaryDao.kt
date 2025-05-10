package com.example.wordwave.presentation.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.example.wordwave.presentation.db.WordWithTranslations
import com.example.wordwave.presentation.db.entities.Translation
import com.example.wordwave.presentation.db.entities.Word

@Dao
interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWord(word: Word): Long

    @Upsert()
    suspend fun upsertTranslations(translations: List<Translation>)

    @Transaction
    suspend fun upsertWordWithTranslations(word: Word, translations: List<String>) {
        val wordId = upsertWord(word).toInt()
        val translationEntities = translations.map {
            Translation(wordId = wordId, value = it)
        }
        upsertTranslations(translationEntities)
    }

    @Transaction
    @Query("SELECT * FROM Word WHERE languageId = :languageId")
    suspend fun getWordsWithTranslationsByLanguage(languageId: Int): List<WordWithTranslations>

    @Query("SELECT * FROM Word WHERE languageId = :languageId")
    suspend fun getWordsByLanguage(languageId: Int): List<Word>

    @Query("UPDATE Word SET progress = :progress WHERE id = :wordId")
    suspend fun updateProgress(wordId: Int, progress: Int)

    @Delete
    suspend fun deleteWord(word: Word)
}