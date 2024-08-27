package com.example.foodiefinder.fragments

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodiefinder.R
import com.example.foodiefinder.activities.MealViewActivity
import com.example.foodiefinder.adapters.MealAdapter
import com.example.foodiefinder.pojo.Meal
import com.example.foodiefinder.network.RetrofitHelper
import com.example.foodiefinder.viewmodels.HomeFactory
import com.example.foodiefinder.viewmodels.HomeViewModel


class HomeFragment : Fragment() {
    private lateinit var cvDailyInspiration : CardView
    private lateinit var tvRandomMeal : TextView
    private lateinit var ivRandomMeal : ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvPopularMeals : RecyclerView
    private lateinit var popularMealsAdapter: MealAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        prepareRecyclerView()
        setupViewModel()
        setupObservers()
    }

    private fun initUI(view : View){
        cvDailyInspiration = view.findViewById(R.id.cvDailyInspiration)
        tvRandomMeal = view.findViewById(R.id.tvRandomMeal)
        ivRandomMeal = view.findViewById(R.id.ivRandomMeal)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        rvPopularMeals = view.findViewById(R.id.rvPopularMeals)
    }


    private fun prepareRecyclerView(){
        popularMealsAdapter = MealAdapter(listOf() , R.layout.item_horizontal_big , requireActivity())
        rvPopularMeals.adapter = popularMealsAdapter
        rvPopularMeals.layoutManager = LinearLayoutManager(requireActivity() , RecyclerView.HORIZONTAL , false)
    }
    private fun setupViewModel(){
        val retrofitService = RetrofitHelper.retrofitService
        val factory = HomeFactory(retrofitService)
        homeViewModel = ViewModelProvider(this , factory).get(HomeViewModel::class.java)
    }
    private fun setupObservers(){
        val mealObserver = Observer<Meal> { randomMeal ->
            showRandomMeal(randomMeal)
            cvDailyInspiration.setOnClickListener {
                val intent = Intent(requireActivity() , MealViewActivity::class.java)
                intent.putExtra("MEAL_ID" , randomMeal.mealId)
                startActivity(intent)
            }
        }
        homeViewModel.randomMeal.observe(viewLifecycleOwner , mealObserver)

        val popularMealsObserver = Observer<List<Meal>>{popularMeals ->
            popularMealsAdapter.meals = popularMeals
            popularMealsAdapter.notifyDataSetChanged()
        }
        homeViewModel.popularMeals.observe(viewLifecycleOwner , popularMealsObserver)
    }

    private fun showRandomMeal(randomMeal : Meal){
        progressBar.visibility = View.GONE
        Glide.with(requireActivity())
            .load(randomMeal.thumbnail)
            .into(ivRandomMeal)

        tvRandomMeal.text = randomMeal.name
    }

}