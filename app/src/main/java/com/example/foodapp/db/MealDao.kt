package com.example.foodapp.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.foodapp.pojo.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM meals_table WHERE userId = :userId")
    suspend fun getMealsByUserId(userId : String) : List<Meal>

    @Insert
    suspend fun addMeal(meal : Meal)

    @Query("DELETE FROM meals_table WHERE mealId = :mealId AND userId = :userId")
    fun removeMeal(mealId: String, userId: String)

    @Query("SELECT EXISTS(SELECT 1 FROM meals_table WHERE mealId = :mealId AND userId = :userId LIMIT 1)")
    fun exists(mealId: String, userId: String): Boolean
}