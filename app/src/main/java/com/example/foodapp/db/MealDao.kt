package com.example.foodapp.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodapp.pojo.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM meals_table")
    suspend fun getAllMeals() : List<Meal>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addFav(meal : Meal) : Long

    @Delete
    suspend fun deleteFav(meal : Meal) : Int

    @Query("SELECT EXISTS(SELECT 1 FROM meals_table WHERE id = :id LIMIT 1)")
    fun isFav(id: String): Boolean
}