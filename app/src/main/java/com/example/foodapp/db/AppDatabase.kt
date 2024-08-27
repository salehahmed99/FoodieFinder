package com.example.foodapp.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.foodapp.pojo.Meal
import com.example.foodapp.pojo.User

@Database(entities = [Meal::class , User::class] , version = 8)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getMealDao() : MealDao
    abstract fun getUserDao() : UserDao

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