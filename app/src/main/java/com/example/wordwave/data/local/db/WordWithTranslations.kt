package com.example.wordwave.data.local.db

import androidx.room.Embedded
import androidx.room.Relation
import com.example.wordwave.data.local.db.entities.Translation
import com.example.wordwave.data.local.db.entities.Word

data class WordWithTranslations(
    @Embedded val word: Word,
    @Relation(
        parentColumn = "id",
        entityColumn = "wordId",
        entity = Translation::class
    )
    val translations: List<TranslationWithSynonyms>
)
