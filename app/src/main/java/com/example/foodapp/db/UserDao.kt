package com.example.foodapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodapp.pojo.User

@Dao
interface UserDao {
    @Query("SELECT name FROM users_table WHERE id = :userId")
    suspend fun getUserNameById(userId : String) : String

    @Insert
    suspend fun addUser(user : User) : Long
}