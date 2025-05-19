package com.example.wordwave.data.local.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "synonym",
    foreignKeys = [ForeignKey(
        entity = Translation::class,
        parentColumns = ["id"],
        childColumns = ["translationId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index("translationId")]  // индекс для FK:contentReference[oaicite:4]{index=4}
)
data class Synonym(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val translationId: Int,
    val text: String
)