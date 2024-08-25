package com.example.foodapp.network

import com.example.foodapp.pojo.CategoryResponse
import com.example.foodapp.pojo.CountryResponse
import com.example.foodapp.pojo.IngredientResponse
import com.example.foodapp.pojo.MealResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface RetrofitService {
    @GET("categories.php")
    suspend fun getAllCategories() : Response<CategoryResponse>

    @GET("list.php?a=list")
    suspend fun getAllCountries() : Response<CountryResponse>

    @GET("list.php?i=list")
    suspend fun getAllIngredients() : Response<IngredientResponse>

    @GET("filter.php")
    suspend fun filterByIngredient(@Query("i") ingredient : String) : Response<MealResponse>

    @GET("filter.php")
    suspend fun filterByCategory(@Query("c") category : String) : Response<MealResponse>

    @GET("filter.php")
    suspend fun filterByCountry(@Query("a") country : String) : Response<MealResponse>


    @GET("search.php")
    suspend fun filterByName(@Query("s") name : String) : Response<MealResponse>

    @GET("lookup.php")
    suspend fun getMealById(@Query("i" )id : String) : Response<MealResponse>

    @GET("random.php")
    suspend fun getRandomMeal() : Response<MealResponse>

}