package com.example.wordwave.presentation.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "words",
    foreignKeys = [
        ForeignKey(
            entity = LocalUser::class,
            parentColumns = ["user_id"],
            childColumns = ["user_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = UserLanguage::class,
            parentColumns = ["id"],
            childColumns = ["language_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index("user_id"),
        Index("language_id")
    ]
)
data class LocalWord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "user_id")
    val userId: String,
    @ColumnInfo(name = "language_id")
    val languageId: Long,
    @ColumnInfo(name = "foreign_word")
    val foreignWord: String,
    val translation: String,
    val examples: String,
    @ColumnInfo(name = "image_url")
    val imageUrl: String? = null,
    @ColumnInfo(name = "is_learned")
    val isLearned: Boolean = false,
    @ColumnInfo(name = "last_updated")
    val lastUpdated: Long = System.currentTimeMillis()
)
