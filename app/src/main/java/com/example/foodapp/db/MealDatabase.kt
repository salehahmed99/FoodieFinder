package com.example.foodapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodapp.pojo.Meal

@Database(entities = [Meal::class] , version = 1)
abstract class MealDatabase : RoomDatabase() {
    abstract fun getMealDao() : MealDao

    companion object{
        @Volatile
        private var INSTANCE : MealDatabase? = null

        fun getInstance(context : Context) : MealDatabase{
            return INSTANCE ?: synchronized(this){
                val tempInstance : MealDatabase = Room.databaseBuilder(
                    context,
                    MealDatabase::class.java,
                    "meal_db"
                ).build()
                INSTANCE = tempInstance
                tempInstance
            }
        }
    }
}