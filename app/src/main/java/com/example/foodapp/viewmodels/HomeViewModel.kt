package com.example.foodapp.viewmodels

import android.util.Log
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

class HomeViewModel(val retrofitService: RetrofitService) : ViewModel(){

    private val _randomMeal: MutableLiveData<Meal> = MutableLiveData()
    val randomMeal: LiveData<Meal> = _randomMeal

    init {
        getRandomMeal()
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
}


class HomeFactory(val retrofitService: RetrofitService) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)){
            return HomeViewModel(retrofitService) as T
        }
        else{
            throw IllegalArgumentException()
        }
    }
}