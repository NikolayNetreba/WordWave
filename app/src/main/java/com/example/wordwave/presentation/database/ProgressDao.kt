package com.example.wordwave.presentation.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface ProgressDao {
    @Insert
    suspend fun insert(progress: Progress)

    @Update
    suspend fun update(progress: Progress)

    @Query("SELECT * FROM progress WHERE word_id = :wordId")
    suspend fun getProgressByWord(wordId: Long): Progress?
}