package com.example.wordwave.data.local.db.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "translations",
    foreignKeys = [ForeignKey(
        entity = Word::class,
        parentColumns = ["id"],
        childColumns = ["wordId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["wordId"])]
)
data class Translation(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wordId: Int,
    val value: String
)
