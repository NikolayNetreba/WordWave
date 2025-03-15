package com.example.wordwave.presentation.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {
    @Insert
    suspend fun insert(user: LocalUser)

    @Query("SELECT * FROM users WHERE user_id = :userId")
    suspend fun getUser(userId: String): LocalUser?

    @Update
    suspend fun updateUser(user: LocalUser)
}