package com.example.foodapp.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.adapters.MealAdapter
import com.example.foodapp.network.RetrofitHelper
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewmodels.AllMealsFactory
import com.example.foodapp.viewmodels.AllMealsViewModel

class AllMealsActivity : AppCompatActivity() {
    private lateinit var tvNoResults : TextView
    private lateinit var tvNoResultsDesc : TextView
    private lateinit var imgBtnBack: ImageButton
    private lateinit var tvAllMeals: TextView
    private lateinit var rvAllMeals: RecyclerView
    private lateinit var mealAdapter: MealAdapter
    private lateinit var allMealsViewModel: AllMealsViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_meals)
        initUI()
        prepareRecyclerView()
        setupViewModel()
        setupObservers()
        val filter = intent.getCharExtra("FILTER", 'f')
        when (filter) {
            'i' -> {
                val ingredientName = intent.getStringExtra("NAME")
                tvAllMeals.text = "Filter by ingredient : ${ingredientName}"
                allMealsViewModel.getMealsByIngredient(ingredientName!!)
            }

            'c' -> {
                val categoryName = intent.getStringExtra("NAME")
                tvAllMeals.text = "Filter by category : ${categoryName}"
                allMealsViewModel.getMealsByCategory(categoryName!!)
            }

            'a' -> {
                val countryName = intent.getStringExtra("NAME")
                tvAllMeals.text = "Filter by country : ${countryName}"
                allMealsViewModel.getMealsByCountry(countryName!!)
            }
            'n'->{
                val mealName = intent.getStringExtra("NAME")
                tvAllMeals.text = "Filter by name : ${mealName}"
                allMealsViewModel.getMealsByName(mealName!!)
            }
        }
        onBackBtnClick()
    }

    private fun initUI() {
        tvNoResults = findViewById(R.id.tvNoResults)
        tvNoResultsDesc = findViewById(R.id.tvNoResultsDesc)
        imgBtnBack = findViewById(R.id.imgBtnBack)
        tvAllMeals = findViewById(R.id.tvAllMeals)
        rvAllMeals = findViewById(R.id.rvAllMeals)
    }

    private fun prepareRecyclerView(){
        mealAdapter = MealAdapter(listOf(), R.layout.item_vertical_big , this)
        rvAllMeals.adapter = mealAdapter
        rvAllMeals.layoutManager = GridLayoutManager(this, 2)
    }

    private fun setupViewModel() {
        val retrofitService = RetrofitHelper.retrofitService
        val factory = AllMealsFactory(retrofitService)
        allMealsViewModel = ViewModelProvider(this, factory).get(AllMealsViewModel::class.java)
    }

    private fun setupObservers() {
        val mealObserver = Observer<List<Meal>> { meals ->
            mealAdapter.meals = meals
            mealAdapter.notifyDataSetChanged()
        }
        allMealsViewModel.meals.observe(this, mealObserver)

        val flagObserver = Observer<Boolean>{
            tvNoResults.visibility = View.VISIBLE
            tvNoResultsDesc.visibility = View.VISIBLE
        }
        allMealsViewModel.flag.observe(this , flagObserver)
    }

    private fun onBackBtnClick() {
        imgBtnBack.setOnClickListener {
            this.finish()
        }
    }
}