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
import com.example.foodiefinder.activities.AllMealsActivity
import com.example.foodiefinder.pojo.Category

class CategoryAdapter(var categories: List<Category> , val layoutRes : Int , val activity : Activity) : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {
    class CategoryViewHolder(val categoryItem : View) : RecyclerView.ViewHolder(categoryItem) {
        val name: TextView = categoryItem.findViewById(R.id.tvName)
        val thumbnail : ImageView = categoryItem.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(layoutRes,parent , false)
        return CategoryViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val currentCategory = categories[position]
        holder.name.text = currentCategory.name
        Glide.with(holder.categoryItem.context)
            .load(currentCategory.thumbnail)
            .into(holder.thumbnail)

        holder.categoryItem.setOnClickListener {
            val intent = Intent(activity , AllMealsActivity::class.java)
            intent.putExtra("FILTER" , 'c')
            intent.putExtra("NAME" , currentCategory.name)
            activity.startActivity(intent)
        }
    }
}