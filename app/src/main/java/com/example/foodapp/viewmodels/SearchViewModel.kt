package com.example.foodapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.foodapp.network.RetrofitService
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.Country
import com.example.foodapp.pojo.Ingredient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchViewModel(val retrofitService: RetrofitService) : ViewModel() {
    private val _categories : MutableLiveData<List<Category>> = MutableLiveData()
    val categories : LiveData<List<Category>> = _categories

    private val _ingredients : MutableLiveData<List<Ingredient>> = MutableLiveData()
    val ingredients : LiveData<List<Ingredient>> = _ingredients

    private val _countries : MutableLiveData<List<Country>> = MutableLiveData()
    val countries : LiveData<List<Country>> = _countries


    fun getAllCategories(){
        viewModelScope.launch(Dispatchers.IO){
            val categoryResonse = retrofitService.getAllCategories().body()
            val myCategories = categoryResonse?.categories
            withContext(Dispatchers.Main) {
                if (!myCategories.isNullOrEmpty()) {
                    _categories.postValue(myCategories!!)
                }
            }
        }
    }

    fun getAllIngredients() {
        viewModelScope.launch(Dispatchers.IO){
            val ingredientsResponse = retrofitService.getAllIngredients().body()
            val myIngredients = ingredientsResponse?.ingredients
            withContext(Dispatchers.Main){
                if (!myIngredients.isNullOrEmpty()){
                    _ingredients.postValue(myIngredients!!)
                }
            }
        }
    }

    fun getAllCountries(){
        viewModelScope.launch(Dispatchers.IO){
            val countryResponse = retrofitService.getAllCountries().body()
            val myCountries = countryResponse?.countries
            withContext(Dispatchers.Main) {
                if (!myCountries.isNullOrEmpty()) {
                    _countries.postValue(myCountries!!)
                }
            }
        }
    }
}

class SearchFactory(val retrofitService: RetrofitService) : ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)){
            return SearchViewModel(retrofitService) as T
        }
        else{
            throw IllegalArgumentException()
        }
    }
}