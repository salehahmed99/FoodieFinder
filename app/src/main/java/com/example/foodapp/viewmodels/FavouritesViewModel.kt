package com.example.foodapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDao
import com.example.foodapp.pojo.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavouritesViewModel(private val mealDao: MealDao) : ViewModel(){
    private val _favouriteMeals: MutableLiveData<List<Meal>> = MutableLiveData()
    val favouriteMeals: LiveData<List<Meal>> = _favouriteMeals

    fun getFavourites(){
        viewModelScope.launch(Dispatchers.IO){
            val myMeals = mealDao.getAllMeals()
            withContext(Dispatchers.Main){
                _favouriteMeals.postValue(myMeals)
            }
        }
    }
}

class FavouritesFactory(private val mealDao : MealDao) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavouritesViewModel::class.java)){
            return FavouritesViewModel(mealDao) as T
        }
        else
            throw IllegalArgumentException()
    }
}