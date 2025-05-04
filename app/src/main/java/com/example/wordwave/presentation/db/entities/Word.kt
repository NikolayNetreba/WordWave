package com.example.wordwave.presentation.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [ForeignKey(
        entity = Language::class,
        parentColumns = ["id"],
        childColumns = ["languageId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("languageId")]
)
data class Word(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val languageId: Int,
    val word: String,
    val translation: String,
    val example: String?,
    val imageUrl: String?,
    val progress: Int
)
