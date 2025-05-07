package com.example.wordwave.data.local.dp

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.wordwave.data.local.dp.dao.LanguageDao
import com.example.wordwave.data.local.dp.dao.UserDao
import com.example.wordwave.data.local.dp.dao.WordDao
import com.example.wordwave.data.local.dp.entities.Language
import com.example.wordwave.data.local.dp.entities.User
import com.example.wordwave.data.local.dp.entities.Word

@Database(entities = [User::class, Language::class, Word::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun languageDao(): LanguageDao
    abstract fun wordDao(): WordDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "dictionary_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
