package com.example.foodapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.adapters.MealAdapter
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.network.RetrofitHelper
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewmodels.AllMealsFactory
import com.example.foodapp.viewmodels.AllMealsViewModel
import com.example.foodapp.viewmodels.FavouritesFactory
import com.example.foodapp.viewmodels.FavouritesViewModel


class FavouritesFragment : Fragment() {
    private lateinit var tvFavourites: TextView
    private lateinit var rvFavourites: RecyclerView
    private lateinit var mealAdapter: MealAdapter
    private lateinit var favouritesViewModel: FavouritesViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        setupViewModel()
        setupObserver()
        favouritesViewModel.getFavourites()
    }

    override fun onResume() {
        super.onResume()
        favouritesViewModel.getFavourites()
    }


    private fun initUI(view : View){
        tvFavourites = view.findViewById(R.id.tvFavourites)
        rvFavourites = view.findViewById(R.id.rvFavourites)
        mealAdapter = MealAdapter(listOf(), R.layout.item_vertical_big , requireActivity())
        rvFavourites.adapter = mealAdapter
        rvFavourites.layoutManager = GridLayoutManager(requireContext(), 2)
    }
    private fun setupViewModel() {
        val mealDao = MealDatabase.getInstance(requireActivity()).getMealDao()
        val factory = FavouritesFactory(mealDao)
        favouritesViewModel = ViewModelProvider(this , factory).get(FavouritesViewModel::class.java)
    }

    private fun setupObserver() {
        val favMealsObserver = Observer<List<Meal>> { favouriteMeals ->
            mealAdapter.meals = favouriteMeals
            mealAdapter.notifyDataSetChanged()
        }
        favouritesViewModel.favouriteMeals.observe(viewLifecycleOwner, favMealsObserver)
    }

}