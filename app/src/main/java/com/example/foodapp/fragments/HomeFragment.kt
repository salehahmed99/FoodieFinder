package com.example.foodapp.fragments

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.Util
import com.example.foodapp.activities.MealViewActivity
import com.example.foodapp.activities.SignInActivity
import com.example.foodapp.adapters.MealAdapter
import com.example.foodapp.pojo.Meal
import com.example.foodapp.network.RetrofitHelper
import com.example.foodapp.viewmodels.HomeFactory
import com.example.foodapp.viewmodels.HomeViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class HomeFragment : Fragment() {
    private lateinit var userPhoto : ImageView
    private lateinit var tvWelcome : TextView
    private lateinit var cvDailyInspiration : CardView
    private lateinit var tvRandomMeal : TextView
    private lateinit var ivRandomMeal : ImageView
    private lateinit var progressBar: ProgressBar
    private lateinit var rvPopularMeals : RecyclerView
    private lateinit var popularMealsAdapter: MealAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = Firebase.auth
        setupFirebase()
        initUI(view)
        showUserName()
        showUserPhoto()
        handleUserPhotoOnClick()
        prepareRecyclerView()
        setupViewModel()
        setupObservers()
    }

    private fun initUI(view : View){
        tvWelcome = view.findViewById(R.id.tvWelcome)
        userPhoto = view.findViewById(R.id.ivUserPhoto)
        cvDailyInspiration = view.findViewById(R.id.cvDailyInspiration)
        tvRandomMeal = view.findViewById(R.id.tvRandomMeal)
        ivRandomMeal = view.findViewById(R.id.ivRandomMeal)
        progressBar = view.findViewById(R.id.progressBar)
        progressBar.visibility = View.VISIBLE
        rvPopularMeals = view.findViewById(R.id.rvPopularMeals)
    }

    private fun showUserName(){
        val firstName = auth.currentUser?.displayName?.split(" ")?.get(0)
        if (firstName != null)
            tvWelcome.text = "Hello, $firstName!"
        else
            tvWelcome.text = "Hello, Guest!"
    }

    private fun showUserPhoto(){
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(requireActivity())
        if (googleSignInAccount != null) {
            val photoUrl = googleSignInAccount.photoUrl
            Glide.with(this)
                .load(photoUrl)
                .into(userPhoto)
        }
    }

    private fun handleUserPhotoOnClick() {
        userPhoto.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null){
                Util.showAlertDialog(
                    "Sign In for More Features",
                    "Add your food preferences, plan your meals and more!" ,
                    "Cancel",
                    "Sign In",
                    requireActivity(),
                    SignInActivity::class.java
                )
            }
            else{
                showLogOutAlertDialog()
            }
        }
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
            showData(randomMeal)
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

    private fun showData(randomMeal : Meal){
        progressBar.visibility = View.GONE
        Glide.with(requireActivity())
            .load(randomMeal.thumbnail)
            .into(ivRandomMeal)

        tvRandomMeal.text = randomMeal.name
    }


    private fun setupFirebase(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
    }
    private fun signOutAndStartSignInActivity() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(requireActivity()) {
            val intent = Intent(requireActivity(), SignInActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun showLogOutAlertDialog(){
        val alertDialog = AlertDialog.Builder(requireActivity() , R.style.CustomAlertDialog)

        alertDialog.setTitle("Are you sure you want to log out?")
            .setMessage("You'll miss out on personalized content and saving our delicious recipes.")
            .setCancelable(false)
            .setNegativeButton(
                "No, Go Back", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            .setPositiveButton("Yes, Sign Out") {dialogInterface, i -> signOutAndStartSignInActivity() }
            .create().show()
    }

}