package com.example.foodapp.activities

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.Util
import com.example.foodapp.adapters.IngredientAdapter
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.network.RetrofitHelper
import com.example.foodapp.pojo.Ingredient
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewmodels.MealFactory
import com.example.foodapp.viewmodels.MealViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class MealViewActivity : AppCompatActivity() {
    private lateinit var imgBtnBack : ImageButton
    private lateinit var fabFav : FloatingActionButton
    private lateinit var ivMeal : ImageView
    private lateinit var tvMealName : TextView
    private lateinit var tvCategory : TextView
    private lateinit var tvCountry : TextView
    private lateinit var tvStepByStep : TextView
    private lateinit var webView : WebView
    private lateinit var rvIngredients: RecyclerView
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var mealViewModel: MealViewModel
    private lateinit var auth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_meal_view)
        auth = Firebase.auth
        initUI()
        setupViewModel()
        setupObservers()

        mealViewModel.getMealByName(intent.getStringExtra("MEAL_NAME")!!)
        onBackBtnClick()

    }

    private fun initUI(){
        imgBtnBack = findViewById(R.id.imgBtnBack)
        fabFav = findViewById(R.id.fabFav)
        ivMeal = findViewById(R.id.ivMeal)
        tvMealName = findViewById(R.id.tvMealName)
        tvCategory = findViewById(R.id.tvCategory)
        tvCountry = findViewById(R.id.tvCountry)
        tvStepByStep = findViewById(R.id.tvStepByStep)
        webView = findViewById(R.id.webView)
        rvIngredients = findViewById(R.id.rvIngredients)
        ingredientAdapter = IngredientAdapter(listOf() , R.layout.item_vertical_small , this)
        rvIngredients.adapter = ingredientAdapter
        rvIngredients.layoutManager = GridLayoutManager(this,2)
    }

    private fun setupViewModel(){
        val retrofitService = RetrofitHelper.retrofitService
        val mealDao = MealDatabase.getInstance(this).getMealDao()
        val factory = MealFactory(retrofitService , mealDao)
        mealViewModel = ViewModelProvider(this , factory).get(MealViewModel::class.java)
    }
    private fun setupObservers(){
        val mealObserver = Observer<Meal> { meal ->
            showData(meal)
            setupWebView(meal)

            val currentUser = auth.currentUser
            if (currentUser != null) {
                mealViewModel.isInFav(meal, currentUser.uid)
            }

            fabFav.setOnClickListener {
                if (currentUser != null)
                    mealViewModel.handleFav(meal, currentUser.uid)
                else{
                    Util.showAlertDialog(
                        "Sign In for More Features",
                        "Add your food preferences, plan your meals and more!" ,
                        "Cancel",
                        "Sign In",
                        this,
                        SignInActivity::class.java
                    )
                }
            }


        }
        mealViewModel.meal.observe(this , mealObserver)

        val msgObserver = Observer<String>{msg ->
            Toast.makeText(this , msg , Toast.LENGTH_SHORT).show()
        }
        mealViewModel.msg.observe(this , msgObserver)

        val isFavObserver = Observer<Boolean>{isFav ->
            if (isFav) {
                fabFav.setImageResource(R.drawable.ic_favourite_fill)
            }
                else{
                    fabFav.setImageResource(R.drawable.ic_favourite_border)
                }
            }

        mealViewModel.isFav.observe(this , isFavObserver)
    }

    private fun showData(meal : Meal){
        Glide.with(this)
            .load(meal.thumbnail)
            .into(ivMeal)

        tvMealName.text = meal.name
        tvCategory.text = "Category:  ${meal.category}"
        tvCountry.text = "Origin Country:  ${meal.country}"
        ingredientAdapter.ingredients  = getValidIngredients(meal)
        ingredientAdapter.notifyDataSetChanged()
        tvStepByStep.text = meal.instructions
    }
    private fun setupWebView(meal : Meal){
        val video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/${getYoutubeCode(meal.youtube)}?si=9VM60AA40EQzV_Mr\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" referrerpolicy=\"strict-origin-when-cross-origin\" allowfullscreen></iframe>"
        webView.loadData(video , "text/html" , "utf-8")
        webView.settings.javaScriptEnabled = true
        webView.webChromeClient = WebChromeClient()
    }
    private fun getValidIngredients(meal : Meal) : List<Ingredient> {
        val ingredientList : List<Ingredient> = listOf(
            Ingredient(meal.ingredient1),
            Ingredient(meal.ingredient2),
            Ingredient(meal.ingredient3),
            Ingredient(meal.ingredient4),
            Ingredient(meal.ingredient5),
            Ingredient(meal.ingredient6),
            Ingredient(meal.ingredient7),
            Ingredient(meal.ingredient8),
            Ingredient(meal.ingredient9),
            Ingredient(meal.ingredient10),
            Ingredient(meal.ingredient11),
            Ingredient(meal.ingredient12),
            Ingredient(meal.ingredient13),
            Ingredient(meal.ingredient14),
            Ingredient(meal.ingredient15),
            Ingredient(meal.ingredient16),
            Ingredient(meal.ingredient17),
            Ingredient(meal.ingredient18),
            Ingredient(meal.ingredient19),
            Ingredient(meal.ingredient20)
        )
        val validIngredientList : MutableList<Ingredient> = mutableListOf()
        for (ingredient in ingredientList)
            if (!ingredient.name.isNullOrBlank())
                validIngredientList.add(ingredient)
        return validIngredientList
    }

    private fun getYoutubeCode(youtube : String?) : String{
        if (youtube.isNullOrBlank())
            return ""
        var i =0
        while(youtube[i] != '=')
            i++

        return youtube.substring(i+1)
    }

    private fun onBackBtnClick(){
        imgBtnBack.setOnClickListener {
            this.finish()
        }
    }

}