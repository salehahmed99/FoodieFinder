package com.example.foodapp.pojo

import com.google.gson.annotations.SerializedName

data class IngredientResponse(
    @SerializedName("meals") val ingredients: List<Ingredient>
)