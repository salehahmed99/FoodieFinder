package com.example.foodapp.activities

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
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
import com.google.android.material.navigation.NavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    private lateinit var navHeader : View
    private lateinit var userPhoto : ImageView
    private lateinit var drawerUserPhoto : ImageView
    private lateinit var tvUserName : TextView
    private lateinit var tvTitle : TextView
    private lateinit var botNavView : BottomNavigationView
    private lateinit var drawerLayout : DrawerLayout
    private lateinit var drawerNavView : NavigationView
    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        auth = Firebase.auth
        initUI()
        setupBotNavMenu()
        setupDrawerNavigationView()
        showUserPhoto(userPhoto)
        handleUserPhotoOnClick()
        setupFirebase()
    }

    private fun initUI(){
        botNavView = findViewById(R.id.bot_nav_view)
        userPhoto = findViewById(R.id.ivUserPhoto)
        tvTitle = findViewById(R.id.tvTitle)
        drawerLayout = findViewById(R.id.drawer_layout)
        drawerNavView = findViewById(R.id.drawer_nav_view)
        navHeader = drawerNavView.getHeaderView(0) //Getting a reference to the nav_header
        tvUserName  = navHeader.findViewById(R.id.tvUserName)
        drawerUserPhoto = navHeader.findViewById(R.id.ivUserPhoto)
    }


    private fun setupBotNavMenu() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.findNavController()
        botNavView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment -> tvTitle.text = "Home"
                R.id.searchFragment -> tvTitle.text = "Search"
                R.id.favouritesFragment -> tvTitle.text = "Favourites"
            }
        }
    }

    private fun setupDrawerNavigationView() {
        toggle = ActionBarDrawerToggle(this , drawerLayout , R.string.navigation_drawer_open , R.string.navigation_drawer_close)
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        drawerNavView.setNavigationItemSelectedListener( object : NavigationView.OnNavigationItemSelectedListener{
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                when(item.itemId){
                    R.id.logout -> {
                        showLogOutAlertDialog()
                        return true
                    }
                    else -> return false
                }
            }
        })
    }

    private fun showUserPhoto(userPhoto : ImageView) {
        val googleSignInAccount = GoogleSignIn.getLastSignedInAccount(this)
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
            if (currentUser == null) {
                Util.showAlertDialog(
                    "Sign In for More Features",
                    "Add your food preferences, plan your meals and more!",
                    "Cancel",
                    "Sign In",
                    this,
                    LauncherActivity::class.java
                )
            }
            else{
                drawerLayout.openDrawer(GravityCompat.START)
                showUserPhoto(drawerUserPhoto)
                tvUserName.text = currentUser.displayName
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