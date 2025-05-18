package com.example.wordwave.data.local.db

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val Context.dataStore by preferencesDataStore(name = "ocr_cache")

object OCRCache {
    private val TEXTS_KEY = stringSetPreferencesKey("cached_texts")

    suspend fun saveText(context: Context, text: String) {
        context.dataStore.edit { prefs ->
            val current = prefs[TEXTS_KEY].orEmpty().toMutableSet()
            current.add(text)
            prefs[TEXTS_KEY] = current
        }
    }

    suspend fun getAllTexts(context: Context): List<String> {
        val prefs = context.dataStore.data.first()
        return prefs[TEXTS_KEY]?.toList().orEmpty()
    }

    suspend fun clearText(context: Context) {
        context.dataStore.edit { prefs ->
            prefs.remove(TEXTS_KEY)
        }
    }
}
