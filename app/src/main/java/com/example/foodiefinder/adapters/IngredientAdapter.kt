package com.example.foodiefinder.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodiefinder.R
import com.example.foodiefinder.activities.AllContentsActivity
import com.example.foodiefinder.activities.AllMealsActivity
import com.example.foodiefinder.activities.MainActivity
import com.example.foodiefinder.pojo.Ingredient

class IngredientAdapter(var ingredients: List<Ingredient> , val layoutRes: Int , val activity : Activity) : RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder>() {
    class IngredientViewHolder(val ingredientItem : View) : RecyclerView.ViewHolder(ingredientItem) {
        val name: TextView = ingredientItem.findViewById(R.id.tvName)
        val thumbnail : ImageView = ingredientItem.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IngredientViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(layoutRes ,parent , false)
        return IngredientViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

    override fun onBindViewHolder(holder: IngredientViewHolder, position: Int) {
        val currentIngredient = ingredients[position]
        holder.name.text = currentIngredient.name
        Glide.with(holder.ingredientItem.context)
            .load("https://www.themealdb.com/images/ingredients/${currentIngredient.name}.png")
            .into(holder.thumbnail)

        if (activity is MainActivity || activity is AllContentsActivity ) {
            holder.ingredientItem.setOnClickListener {
                val intent = Intent(activity, AllMealsActivity::class.java)
                intent.putExtra("FILTER", 'i')
                intent.putExtra("NAME", currentIngredient.name)
                activity.startActivity(intent)
            }
        }
    }
}