package com.example.wordwave.presentation.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.wordwave.presentation.db.entities.Language

@Dao
interface LanguageDao {
    @Upsert()
    suspend fun upsertLanguage(language: Language)

    @Query("SELECT * FROM Language WHERE userId = :userId")
    suspend fun getLanguagesByUser(userId: String): List<Language>
}
