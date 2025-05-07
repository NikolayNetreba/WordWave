package com.example.wordwave.data.local.dp.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.example.wordwave.data.local.dp.entities.Word

@Dao
interface WordDao {
    @Upsert()
    suspend fun upsertWord(word: Word)

    @Query("SELECT * FROM Word WHERE languageId = :languageId")
    suspend fun getWordsByLanguage(languageId: Int): List<Word>

    @Query("UPDATE Word SET progress = :progress WHERE id = :wordId")
    suspend fun updateProgress(wordId: Int, progress: Int)

    @Delete
    suspend fun deleteWord(word: Word)
}
