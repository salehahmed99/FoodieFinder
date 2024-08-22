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

class AllMealsViewModel(val retrofitService: RetrofitService) : ViewModel() {
    private val _meals: MutableLiveData<List<Meal>> = MutableLiveData()
    val meals: LiveData<List<Meal>> = _meals

    private val _flag : MutableLiveData<Boolean> = MutableLiveData()
    val flag : MutableLiveData<Boolean> = _flag


    fun getMealsByIngredient(name : String){
        viewModelScope.launch(Dispatchers.IO){
            val mealResponse = retrofitService.filterByIngredient(name).body()
            val myMeals = mealResponse?.meals
            if (!myMeals.isNullOrEmpty())
                _meals.postValue(myMeals!!)
        }
    }

    fun getMealsByCateory(name : String){
        viewModelScope.launch(Dispatchers.IO){
            val mealResponse = retrofitService.filterByCategory(name).body()
            val myMeals = mealResponse?.meals
            if (!myMeals.isNullOrEmpty())
                _meals.postValue(myMeals!!)
        }
    }

    fun getMealsByCountry(name : String){
        viewModelScope.launch(Dispatchers.IO){
            val mealResponse = retrofitService.filterByCountry(name).body()
            val myMeals = mealResponse?.meals
            if (!myMeals.isNullOrEmpty())
                _meals.postValue(myMeals!!)
        }
    }

    fun getMealsByName(name : String){
        viewModelScope.launch(Dispatchers.IO){
            val mealResponse = retrofitService.filterByName(name).body()
            val myMeals = mealResponse?.meals
            withContext(Dispatchers.Main){
                if (!myMeals.isNullOrEmpty()){
                    _meals.postValue(myMeals!!)
                }
                else{
                    _flag.postValue(true)
                }
            }
        }
    }
}

class AllMealsFactory(val retrofitService: RetrofitService) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllMealsViewModel::class.java)){
            return AllMealsViewModel(retrofitService) as T
        }
        else{
            throw IllegalArgumentException()
        }
    }
}