package com.example.wordwave.data.local.db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "word")
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val text: String,
    val language: String,
    val imagePath: String? = null
)