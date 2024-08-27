package com.example.foodiefinder.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://www.themealdb.com/api/json/v1/1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val retrofitService : RetrofitService = retrofit.create(RetrofitService::class.java)
}