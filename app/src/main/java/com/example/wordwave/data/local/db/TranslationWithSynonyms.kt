package com.example.wordwave.data.local.db

import androidx.room.Embedded
import androidx.room.Relation
import com.example.wordwave.data.local.db.entities.Synonym
import com.example.wordwave.data.local.db.entities.Translation

data class TranslationWithSynonyms(
    @Embedded val translation: Translation,
    @Relation(
        parentColumn = "id",
        entityColumn = "translationId",
        entity = Synonym::class
    )
    val synonyms: List<Synonym>
)
