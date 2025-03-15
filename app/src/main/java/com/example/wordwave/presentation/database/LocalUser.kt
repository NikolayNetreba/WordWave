package com.example.wordwave.presentation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class LocalUser(
    @PrimaryKey
    @ColumnInfo(name = "user_id")
    val userId: String,
    val baseLanguage: String, // Mother tongue

    @ColumnInfo(name = "learning_languages")
    val learningLanguages: String
)
