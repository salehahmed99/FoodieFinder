package com.example.foodiefinder.pojo

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    @SerializedName("strArea") val name: String
) : Parcelable