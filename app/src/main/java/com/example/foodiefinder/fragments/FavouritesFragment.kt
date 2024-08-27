package com.example.foodiefinder.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiefinder.R
import com.example.foodiefinder.Util
import com.example.foodiefinder.activities.LauncherActivity
import com.example.foodiefinder.adapters.MealAdapter
import com.example.foodiefinder.db.AppDatabase
import com.example.foodiefinder.pojo.Meal
import com.example.foodiefinder.viewmodels.FavouritesFactory
import com.example.foodiefinder.viewmodels.FavouritesViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class FavouritesFragment : Fragment() {
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
    }

    override fun onResume() {
        super.onResume()
        // This function is here since I want to update the favourites
        // every time the favourites fragment gains focus again
        showFavourites()
    }
    private fun initUI(view : View){
        rvFavourites = view.findViewById(R.id.rvFavourites)
    }

    private fun prepareRecyclerView(){
        mealAdapter = MealAdapter(listOf(), R.layout.item_vertical_big , requireActivity())
        rvFavourites.adapter = mealAdapter
        rvFavourites.layoutManager = GridLayoutManager(requireContext(), 2)
    }
    private fun setupViewModel() {
        val mealDao = AppDatabase.getInstance(requireActivity()).getMealDao()
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
                LauncherActivity::class.java
            )
        }
    }


}