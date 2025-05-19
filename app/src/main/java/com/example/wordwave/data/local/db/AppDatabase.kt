package com.example.wordwave.data.local.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wordwave.data.local.db.dao.DictionaryDao
import com.example.wordwave.data.local.db.dao.LanguageDao
import com.example.wordwave.data.local.db.dao.UserDao
import com.example.wordwave.data.local.db.entities.Language
import com.example.wordwave.data.local.db.entities.Synonym
import com.example.wordwave.data.local.db.entities.Translation
import com.example.wordwave.data.local.db.entities.User
import com.example.wordwave.data.local.db.entities.Word

@Database(entities = [User::class, Language::class, Word::class, Translation::class, Synonym::class], version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun languageDao(): LanguageDao
    abstract fun dictionaryDao(): DictionaryDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dictionary_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
