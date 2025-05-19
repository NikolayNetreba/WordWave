package com.example.wordwave.data.local.db.dao

import androidx.room.*
import com.example.wordwave.data.local.db.WordWithTranslations
import com.example.wordwave.data.local.db.entities.Synonym
import com.example.wordwave.data.local.db.entities.Translation
import com.example.wordwave.data.local.db.entities.Word

@Dao
interface DictionaryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWord(word: Word): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTranslation(translation: Translation): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSynonyms(synonyms: List<Synonym>): List<Long>

    @Transaction
    suspend fun upsertWordWithTranslations(
        word: Word,
        translationsWithSynonyms: Map<String, List<String>>
    ) {
        val wordId = upsertWord(word)

        translationsWithSynonyms.forEach { (translationText, synonymsList) ->
            val translation = Translation(
                wordId = wordId.toInt(),
                translatedText = translationText,
                partOfSpeech = "unknown",
                language = word.language
            )

            val translationId = upsertTranslation(translation)

            val synonymEntities = synonymsList.map { synonymText ->
                Synonym(translationId = translationId.toInt(), text = synonymText)
            }

            upsertSynonyms(synonymEntities)
        }
    }

    @Transaction
    @Query("SELECT * FROM Word")  // or SELECT * FROM WordEntityTable
    fun getAllWordsWithTranslations(): List<WordWithTranslations>

    @Query("SELECT * FROM Word WHERE language = :language")
    suspend fun getWordsByLanguage(language: String): List<Word>

    @Delete
    suspend fun deleteWord(word: Word)
}