package com.example.foodapp.pojo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class User(
    @PrimaryKey val id : String,
    val name : String
)