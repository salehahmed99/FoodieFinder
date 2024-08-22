package com.example.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.activities.MealViewActivity
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.viewmodels.MealFactory
import com.example.foodapp.viewmodels.MealViewModel
import com.example.foodapp.pojo.Meal
import com.example.foodapp.network.RetrofitHelper


class HomeFragment : Fragment() {
    private lateinit var cvDailyInspiration : CardView
    private lateinit var tvRandomMeal : TextView
    private lateinit var ivRandomMeal : ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var mealViewModel: MealViewModel
    private lateinit var randomMeal : Meal
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI(view)
        setupViewModel()
        mealViewModel.getRandomMeal()
        setupObserver()
        cvDailyInspiration.setOnClickListener {
            val intent = Intent(requireActivity() , MealViewActivity::class.java)
            intent.putExtra("MEAL_NAME" , randomMeal.name)
            startActivity(intent)
        }
    }

    private fun initUI(view : View){
        cvDailyInspiration = view.findViewById(R.id.cvDailyInspiration)
        tvRandomMeal = view.findViewById(R.id.tvRandomMeal)
        ivRandomMeal = view.findViewById(R.id.ivRandomMeal)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
    }
    private fun setupViewModel(){
        val retrofitService = RetrofitHelper.retrofitService
        val mealDao = MealDatabase.getInstance(requireActivity()).getMealDao()
        val factory = MealFactory(retrofitService , mealDao)
        mealViewModel = ViewModelProvider(this , factory).get(MealViewModel::class.java)
    }
    private fun setupObserver(){
        val mealObserver = Observer<Meal> { randomMeal ->
            this.randomMeal = randomMeal
            showData(randomMeal) }
        mealViewModel.randomMeal.observe(viewLifecycleOwner , mealObserver)
    }

    private fun showData(randomMeal : Meal){
        progressBar.visibility = View.GONE
        Glide.with(requireActivity())
            .load(randomMeal.thumbnail)
            .into(ivRandomMeal)

        tvRandomMeal.text = randomMeal.name
    }
}