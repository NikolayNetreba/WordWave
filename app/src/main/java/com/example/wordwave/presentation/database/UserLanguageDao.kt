package com.example.wordwave.presentation.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserLanguageDao {
    @Insert
    suspend fun insert(language: UserLanguage)

    @Query("DELETE FROM user_languages WHERE id = :id")
    suspend fun delete(id: Long)

    @Query("SELECT * FROM user_languages WHERE user_id = :userId")
    suspend fun getLanguagesByUser(userId: String): List<UserLanguage>
}