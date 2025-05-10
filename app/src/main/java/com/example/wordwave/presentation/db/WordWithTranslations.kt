package com.example.wordwave.presentation.db

import androidx.room.Embedded
import androidx.room.Relation
import com.example.wordwave.presentation.db.entities.Translation
import com.example.wordwave.presentation.db.entities.Word

data class WordWithTranslations(
    @Embedded val word: Word,
    @Relation(
        parentColumn = "id",
        entityColumn = "wordId"
    )
    val translations: List<Translation>
)
