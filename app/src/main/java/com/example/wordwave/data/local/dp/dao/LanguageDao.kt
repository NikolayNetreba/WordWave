package com.example.wordwave.data.local.dp.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.wordwave.data.local.dp.entities.Language

@Dao
interface LanguageDao {
    @Upsert()
    suspend fun insertLanguage(language: Language)

    @Query("SELECT * FROM Language WHERE userId = :userId")
    suspend fun getLanguagesByUser(userId: String): List<Language>
}
