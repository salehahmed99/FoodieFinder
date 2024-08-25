package com.example.foodapp.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodapp.db.MealDao
import com.example.foodapp.pojo.Meal
import com.example.foodapp.network.RetrofitService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MealViewModel(
    private val retrofitService: RetrofitService,
    private val mealDao: MealDao
) :
    ViewModel() {

    private val _meal: MutableLiveData<Meal> = MutableLiveData()
    val meal: LiveData<Meal> = _meal

    private val _msg: MutableLiveData<String> = MutableLiveData()
    val msg: LiveData<String> = _msg

    private val _isFav: MutableLiveData<Boolean> = MutableLiveData()
    val isFav: LiveData<Boolean> = _isFav


    fun getMealByMealId(mealId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val mealResponse = retrofitService.getMealById(mealId).body()
            val myMeal = mealResponse?.meals?.get(0)
            withContext(Dispatchers.Main) {
                if (myMeal != null) {
                    _meal.postValue(myMeal!!)
                }
            }
        }
    }

    fun handleFav(mealId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val exists = mealDao.exists(mealId, userId)
            if (!exists) {
                val mealResponse = retrofitService.getMealById(mealId).body()
                val myMeal = mealResponse?.meals?.get(0)
                if (myMeal != null) {
                    myMeal.userId = userId
                    mealDao.addMeal(myMeal)
                }
                withContext(Dispatchers.Main) {
                    _msg.postValue("Added to favourites")
                }
            } else {
                mealDao.removeMeal(mealId, userId)
                withContext(Dispatchers.Main) {
                    _msg.postValue("Removed from favourites")
                }
            }
            withContext(Dispatchers.Main) {
                isInFav(mealId , userId)
            }
        }
    }

    fun isInFav(mealId: String, userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val exists = mealDao.exists(mealId, userId)
            withContext(Dispatchers.Main) {
                _isFav.postValue(exists)
            }

        }
    }

}

class MealFactory(
    private val retrofitService: RetrofitService,
    private val mealDao: MealDao,
) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealViewModel::class.java)) {
            return MealViewModel(retrofitService, mealDao) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}
