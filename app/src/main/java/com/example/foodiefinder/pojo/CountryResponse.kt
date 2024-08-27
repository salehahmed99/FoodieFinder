package com.example.foodiefinder.pojo

import com.google.gson.annotations.SerializedName

data class CountryResponse(
    @SerializedName("meals") val countries: List<Country>
)