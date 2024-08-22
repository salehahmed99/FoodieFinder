package com.example.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodapp.R
import com.example.foodapp.activities.AllContentsActivity
import com.example.foodapp.activities.AllMealsActivity
import com.example.foodapp.activities.MealViewActivity
import com.example.foodapp.adapters.CategoryAdapter
import com.example.foodapp.adapters.CountryAdapter
import com.example.foodapp.adapters.IngredientAdapter
import com.example.foodapp.network.RetrofitHelper
import com.example.foodapp.pojo.Category
import com.example.foodapp.pojo.Country
import com.example.foodapp.pojo.Ingredient
import com.example.foodapp.viewmodels.SearchFactory
import com.example.foodapp.viewmodels.SearchViewModel


class SearchFragment : Fragment() {
    private lateinit var searchBar : SearchView
    private lateinit var tvViewAllIngredients : TextView
    private lateinit var tvViewAllCategories : TextView
    private lateinit var tvViewAllCountries : TextView
    private lateinit var rvCategories : RecyclerView
    private lateinit var rvCountries : RecyclerView
    private lateinit var rvIngredients : RecyclerView
    private lateinit var categoryAdapter: CategoryAdapter
    private lateinit var countryAdapter: CountryAdapter
    private lateinit var ingredientAdapter: IngredientAdapter
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var allIngredients : List<Ingredient>
    private lateinit var allCategories : List<Category>
    private lateinit var allCountries : List<Country>
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        setupViewModel()
        setupObservers()
        searchViewModel.getAllCategories()
        searchViewModel.getAllIngredients()
        searchViewModel.getAllCountries()

        tvViewAllIngredients.setOnClickListener{
            val intent = Intent(requireActivity() , AllContentsActivity::class.java)
            intent.putExtra("INGREDIENTS" , allIngredients.toTypedArray())
            intent.putExtra("CHOICE" , 1)
            startActivity(intent)
        }

        tvViewAllCategories.setOnClickListener{
            val intent = Intent(requireActivity() , AllContentsActivity::class.java)
            intent.putExtra("CATEGORIES" , allCategories.toTypedArray())
            intent.putExtra("CHOICE" , 2)
            startActivity(intent)
        }

        tvViewAllCountries.setOnClickListener{
            val intent = Intent(requireActivity() , AllContentsActivity::class.java)
            intent.putExtra("COUNTRIES" , allCountries.toTypedArray())
            intent.putExtra("CHOICE" , 3)
            startActivity(intent)
        }


        searchBar.setOnQueryTextListener(object : OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                val intent = Intent(requireActivity() , AllMealsActivity::class.java)
                intent.putExtra("NAME" , query)
                intent.putExtra("FILTER", 'n')
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

    }

    private fun initUI(view : View){

        searchBar = view.findViewById(R.id.search_bar)
        tvViewAllIngredients = view.findViewById(R.id.tvViewAllIngredients)
        tvViewAllCategories = view.findViewById(R.id.tvViewAllCategories)
        tvViewAllCountries = view.findViewById(R.id.tvViewAllCountries)

        rvCategories = view.findViewById(R.id.rvCategories)
        categoryAdapter = CategoryAdapter(listOf() , R.layout.item_horizontal_big , requireActivity())
        rvCategories.adapter = categoryAdapter
        rvCategories.layoutManager = LinearLayoutManager(requireActivity() , RecyclerView.HORIZONTAL , false)

        rvIngredients = view.findViewById(R.id.rvIngredients)
        ingredientAdapter = IngredientAdapter(listOf() , R.layout.item_horizontal_small , requireActivity())
        rvIngredients.adapter = ingredientAdapter
        rvIngredients.layoutManager = LinearLayoutManager(requireActivity() , RecyclerView.HORIZONTAL , false)

        rvCountries = view.findViewById(R.id.rvCountries)
        countryAdapter = CountryAdapter(listOf() , R.layout.item_horizontal_small , requireActivity())
        rvCountries.adapter = countryAdapter
        rvCountries.layoutManager = LinearLayoutManager(requireActivity() , RecyclerView.HORIZONTAL , false)


    }

    private fun setupViewModel(){
        val retrofitService = RetrofitHelper.retrofitService
        val factory = SearchFactory(retrofitService)
        searchViewModel = ViewModelProvider(this , factory).get(SearchViewModel::class.java)
    }

    private fun setupObservers(){
        val ingredientObserver = Observer<List<Ingredient>>{ingredients ->
            allIngredients = ingredients
            ingredientAdapter.ingredients = ingredients.subList(0,10)
            ingredientAdapter.notifyDataSetChanged()
        }
        searchViewModel.ingredients.observe(viewLifecycleOwner , ingredientObserver)

        val categoryObserver = Observer<List<Category>>{categories ->
            allCategories = categories
            categoryAdapter.categories = categories.subList(0,5)
            categoryAdapter.notifyDataSetChanged()
        }
        searchViewModel.categories.observe(viewLifecycleOwner , categoryObserver)

        val countryObserver = Observer<List<Country>>{ countries ->
            allCountries = countries.subList(0,countries.size - 2)  + listOf(countries.last())
            countryAdapter.countries = countries.subList(0,10)
            countryAdapter.notifyDataSetChanged()
        }
        searchViewModel.countries.observe(viewLifecycleOwner , countryObserver)
    }



}