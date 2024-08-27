package com.example.foodiefinder.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodiefinder.pojo.Meal

@Database(entities = [Meal::class] , version = 9)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMealDao() : MealDao

    companion object{
        @Volatile
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context : Context) : AppDatabase{
            return INSTANCE ?: synchronized(this){
                val tempInstance : AppDatabase = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "meal_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = tempInstance
                tempInstance
            }
        }
    }
}