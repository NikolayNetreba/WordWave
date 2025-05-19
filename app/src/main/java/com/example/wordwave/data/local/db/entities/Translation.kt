package com.example.wordwave.data.local.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "translation",
    foreignKeys = [ForeignKey(
        entity = Word::class,
        parentColumns = ["id"],
        childColumns = ["wordId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("wordId")]  // индекс для ускорения запросов по FK:contentReference[oaicite:3]{index=3}
)
data class Translation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wordId: Int,
    val translatedText: String,
    val partOfSpeech: String,     // например: "noun", "verb" и т.д.
    val language: String
)