package com.example.foodapp.activities

import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.adapters.CategoryAdapter
import com.example.foodapp.adapters.CountryAdapter
import com.example.foodapp.adapters.IngredientAdapter
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.Country
import com.example.foodapp.pojo.Ingredient

class AllContentsActivity : AppCompatActivity() {
    private lateinit var imgBtnBack : ImageButton
    private lateinit var tvAllContents : TextView
    private lateinit var rvAllContents : RecyclerView
    private lateinit var allIngredientsAdapter : IngredientAdapter
    private lateinit var allCategoriesAdapter : CategoryAdapter
    private lateinit var allCountriesAdapter : CountryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_contents)
        initUI()
        val choice = intent.getIntExtra("CHOICE" , -1)
        when(choice){
            1 -> {          //View all ingredients
                tvAllContents.text = "All Ingredients"
                allIngredientsAdapter.ingredients = intent.getParcelableArrayExtra("INGREDIENTS")?.toList() as List<Ingredient>
                rvAllContents.adapter = allIngredientsAdapter
            }
            2 -> {          //View all categories
                tvAllContents.text = "All Categories"
                allCategoriesAdapter.categories = intent.getParcelableArrayExtra("CATEGORIES")?.toList() as List<Category>
                rvAllContents.adapter = allCategoriesAdapter
            }
            3 -> {          //View all countries
                tvAllContents.text = "All Countries"
                allCountriesAdapter.countries = intent.getParcelableArrayExtra("COUNTRIES")?.toList() as List<Country>
                rvAllContents.adapter = allCountriesAdapter
            }
        }
        onBackBtnClick()
    }


    private fun initUI(){
        imgBtnBack = findViewById(R.id.imgBtnBack)
        tvAllContents = findViewById(R.id.tvAllContents)
        rvAllContents = findViewById(R.id.rvAllContents)
        allIngredientsAdapter = IngredientAdapter(listOf() , R.layout.item_vertical_small , this)
        allCategoriesAdapter = CategoryAdapter(listOf() , R.layout.item_vertical_small , this)
        allCountriesAdapter = CountryAdapter(listOf() , R.layout.item_vertical_small , this)
        rvAllContents.layoutManager = GridLayoutManager(this,2)
    }

    private fun onBackBtnClick(){
        imgBtnBack.setOnClickListener {
            this.finish()
        }
    }
}