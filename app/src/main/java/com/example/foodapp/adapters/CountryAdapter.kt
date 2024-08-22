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
import com.example.foodapp.activities.AllMealsActivity
import com.example.foodapp.pojo.Country
import java.util.Locale

class CountryAdapter(var countries: List<Country> , val layoutRes : Int , val activity : Activity) : RecyclerView.Adapter<CountryAdapter.CountryViewHolder>() {
    class CountryViewHolder(val countryItem : View) : RecyclerView.ViewHolder(countryItem) {
        val name: TextView = countryItem.findViewById(R.id.tvName)
        val thumbnail : ImageView = countryItem.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
        val layout = LayoutInflater.from(parent.context).inflate(layoutRes , parent , false)
        return CountryViewHolder(layout)
    }

    override fun getItemCount(): Int {
        return countries.size
    }

    override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
        val currentCountry = countries[position]
        holder.name.text = currentCountry.name

        val resourceId = holder.countryItem.context.resources.getIdentifier(
            currentCountry.name.lowercase(),
            "drawable",
            holder.countryItem.context.packageName
        )
        holder.thumbnail.setImageResource(resourceId)

        holder.countryItem.setOnClickListener {
            val intent = Intent(activity , AllMealsActivity::class.java)
            intent.putExtra("FILTER" , 'a')
            intent.putExtra("NAME" , currentCountry.name)
            activity.startActivity(intent)
        }
    }
}