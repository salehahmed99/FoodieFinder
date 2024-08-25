package com.example.foodapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodapp.network.RetrofitService
import com.example.foodapp.pojo.Meal
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(val retrofitService: RetrofitService) : ViewModel() {

    private val _randomMeal: MutableLiveData<Meal> = MutableLiveData()
    val randomMeal: LiveData<Meal> = _randomMeal

    private val _populareMeals: MutableLiveData<List<Meal>> = MutableLiveData()
    val popularMeals: LiveData<List<Meal>> = _populareMeals

    init {
        getRandomMeal()
        getPopularMeals()
    }

    private fun getRandomMeal() {
        viewModelScope.launch(Dispatchers.IO) {
            val randomMealResponse = retrofitService.getRandomMeal().body()
            val myMeal = randomMealResponse?.meals?.get(0)
            withContext(Dispatchers.Main) {
                if (myMeal != null)
                    _randomMeal.postValue(myMeal!!)
            }
        }
    }

    private fun getPopularMeals() {
        viewModelScope.launch(Dispatchers.IO) {

            val popularMealList: MutableList<Meal> = mutableListOf()
            repeat(10) {
                val randomMealResponse = retrofitService.getRandomMeal().body()
                val randomMeal = randomMealResponse?.meals?.get(0)
                if (randomMeal != null)
                    popularMealList.add(randomMeal)
            }
            withContext(Dispatchers.Main) {
                _populareMeals.postValue(popularMealList)
            }
        }
    }
}


class HomeFactory(val retrofitService: RetrofitService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(retrofitService) as T
        } else {
            throw IllegalArgumentException()
        }
    }
}