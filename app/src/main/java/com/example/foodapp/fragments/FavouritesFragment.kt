package com.example.foodapp.fragments

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.Util
import com.example.foodapp.activities.MainActivity
import com.example.foodapp.activities.SignInActivity
import com.example.foodapp.adapters.MealAdapter
import com.example.foodapp.db.MealDatabase
import com.example.foodapp.pojo.Meal
import com.example.foodapp.viewmodels.FavouritesFactory
import com.example.foodapp.viewmodels.FavouritesViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class FavouritesFragment : Fragment() {
    private lateinit var tvFavourites: TextView
    private lateinit var rvFavourites: RecyclerView
    private lateinit var mealAdapter: MealAdapter
    private lateinit var favouritesViewModel: FavouritesViewModel
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_favourites, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        initUI(view)
        prepareRecyclerView()
        setupViewModel()
        setupObserver()
        showFavourites()
    }

    override fun onResume() {
        super.onResume()
        showFavourites()        //This is to update the favourites when the user clicks on a meal and goes back
    }
    private fun initUI(view : View){
        tvFavourites = view.findViewById(R.id.tvFavourites)
        rvFavourites = view.findViewById(R.id.rvFavourites)
    }

    private fun prepareRecyclerView(){
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

    private fun showFavourites(){
        val currentUser = auth.currentUser
        if (currentUser != null)
            favouritesViewModel.getFavouritesByUserId(currentUser.uid)
        else{
            Util.showAlertDialog(
                "Sign In for More Features",
                "Add your food preferences, plan your meals and more!" ,
                "Cancel",
                "Sign In",
                requireActivity(),
                SignInActivity::class.java
            )
        }
    }


}