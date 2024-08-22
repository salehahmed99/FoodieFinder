package com.example.foodapp.pojo

import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("meals") val countries: List<Country>
)