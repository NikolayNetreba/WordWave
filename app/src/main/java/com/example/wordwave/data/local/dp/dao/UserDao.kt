package com.example.wordwave.data.local.dp.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.example.wordwave.data.local.dp.entities.User

@Dao
interface UserDao {
    @Upsert()
    suspend fun upsertUser(user: User)

    @Query("SELECT * FROM User WHERE id = :userId")
    suspend fun getUserById(userId: String): User?
}
