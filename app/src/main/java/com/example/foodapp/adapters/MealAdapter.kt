package com.example.foodapp.adapters

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.activities.MealViewActivity
import com.example.foodapp.pojo.Meal

class MealAdapter(var meals: List<Meal> , val layoutRes : Int , val activity : Activity) : RecyclerView.Adapter<MealAdapter.MealViewHolder>() {
    class MealViewHolder(val mealItem : View) : RecyclerView.ViewHolder(mealItem) {
        val name: TextView = mealItem.findViewById(R.id.tvName)
        val image : ImageView = mealItem.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MealViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(layoutRes ,parent , false)
        return MealViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return meals.size
    }

    override fun onBindViewHolder(holder: MealViewHolder, position: Int) {
        val currentMeal = meals[position]
        holder.name.text = currentMeal.name
        Glide.with(holder.mealItem.context)
            .load(currentMeal.thumbnail)
            .into(holder.image)

        //holder.image.scaleType = ImageView.ScaleType.CENTER_CROP

        holder.mealItem.setOnClickListener {
            val intent = Intent(activity , MealViewActivity::class.java)
            intent.putExtra("MEAL_NAME" , currentMeal.name)
            activity.startActivity(intent)
        }

    }
}