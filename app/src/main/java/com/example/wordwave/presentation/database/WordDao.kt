package com.example.wordwave.presentation.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WordDao {
    @Insert
    suspend fun insert(word: LocalWord)

    @Update
    suspend fun update(word: LocalWord)

    @Query("DELETE FROM words WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM words WHERE user_id = :userId AND language_id = :languageId")
    suspend fun getWordsByLanguage(userId: String, languageId: Long): List<LocalWord>

    @Query("SELECT * FROM words WHERE user_id = :userId AND is_learned = 1")
    suspend fun getLearnedWords(userId: String): List<LocalWord>
}