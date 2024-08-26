package com.example.foodapp.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.foodapp.R
import com.example.foodapp.Util
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    private lateinit var userPhoto : ImageView
    private lateinit var tvTitle : TextView
    private lateinit var botNavView : BottomNavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        initUI()
        showUserPhoto()
        handleUserPhotoOnClick()
        setupBotNavMenu()
        setupFirebase()
    }
    private fun initUI(){
        botNavView = findViewById(R.id.bot_nav_view)
        userPhoto = findViewById(R.id.ivUserPhoto)
        tvTitle = findViewById(R.id.tvTitle)
    }

    private fun setupBotNavMenu(){
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()

        botNavView.setOnItemSelectedListener(object : NavigationBarView.OnItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.homeFragment -> {
                        navController.navigate(R.id.homeFragment)
                        tvTitle.text = "Home"
                        return true
                    }
                    R.id.searchFragment -> {
                        navController.navigate(R.id.searchFragment)
                        tvTitle.text = "Search"
                        return true
                    }
                    R.id.favouritesFragment -> {
                        navController.navigate(R.id.favouritesFragment)
                        tvTitle.text = "Favourites"
                        return true
                    }
                    else ->
                        return false
                }
            }
        })
    }


    fun showUserPhoto() {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
        if (googleSignInAccount != null) {
            val photoUrl = googleSignInAccount.photoUrl
            Glide.with(this)
                .load(photoUrl)
                .into(userPhoto)
        }
    }

    fun handleUserPhotoOnClick() {
        userPhoto.setOnClickListener {
            val currentUser = auth.currentUser
            if (currentUser == null) {
                Util.showAlertDialog(
                    "Sign In for More Features",
                    "Add your food preferences, plan your meals and more!",
                    "Cancel",
                    "Sign In",
                    this,
                    LauncherActivity::class.java
                )
            } else {
                showLogOutAlertDialog()
            }
        }
    }

    private fun setupFirebase(){
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }
    private fun signOutAndStartSignInActivity() {
        auth.signOut()
        googleSignInClient.signOut().addOnCompleteListener(this) {
            val intent = Intent(this, LauncherActivity::class.java)
            startActivity(intent)
            this.finish()
        }
    }

    private fun showLogOutAlertDialog(){
        val alertDialog = AlertDialog.Builder(this, R.style.CustomAlertDialog)

        alertDialog.setTitle("Log Out")
            .setMessage("Are you sure you want to log out?")
            .setCancelable(false)
            .setNegativeButton(
                "Cancel", DialogInterface.OnClickListener { dialogInterface, i ->
                    dialogInterface.cancel()
                })
            .setPositiveButton("Yes") {dialogInterface, i -> signOutAndStartSignInActivity() }
            .create().show()
    }

}