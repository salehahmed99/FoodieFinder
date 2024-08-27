package com.example.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.activities.MainActivity
import com.example.foodapp.db.AppDatabase
import com.example.foodapp.viewmodels.UserFactory
import com.example.foodapp.viewmodels.UserViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class LoginFragment : Fragment() {
    private lateinit var imgBtnBack : ImageButton
    private lateinit var etEmail : TextInputEditText
    private lateinit var etPass : TextInputEditText
    private lateinit var btnStartCooking : Button
    private lateinit var auth : FirebaseAuth
    private lateinit var userViewModel: UserViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        auth = Firebase.auth
        setupViewModel()
        setupObserver()
        onStartCookingBtnClick()
        onBackBtnClick()
    }

    private fun initUI(view: View) {
        imgBtnBack = view.findViewById(R.id.imgBtnBack)
        etEmail = view.findViewById(R.id.etEmail)
        etPass = view.findViewById(R.id.etPass)
        btnStartCooking = view.findViewById(R.id.btnStartCooking)
    }

    private fun setupViewModel(){
        val userDao = AppDatabase.getInstance(requireActivity()).getUserDao()
        val factory = UserFactory(userDao)
        userViewModel = ViewModelProvider(this , factory).get(UserViewModel::class.java)
    }

    private fun setupObserver(){
        val userNameObserver = Observer<String>{ name->
            Toast.makeText(requireActivity(), "Signed in as $name", Toast.LENGTH_SHORT).show()
        }
        userViewModel.name.observe(viewLifecycleOwner , userNameObserver)
    }

    private fun onStartCookingBtnClick() {
        btnStartCooking.setOnClickListener {
            val email = etEmail.text.toString()
            val pass = etPass.text.toString()
            if (email.isBlank()) {
                Toast.makeText(activity, "Enter your Email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass.isBlank()) {
                Toast.makeText(activity, "Enter your Password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            signinWithFirebase(email , pass)
        }
    }
    private fun signinWithFirebase(email : String , pass : String){
        auth.signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    userViewModel.getUserNameById(auth.currentUser?.uid!!)
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                } else {
                    Toast.makeText(activity,"Invalid Email or Password!", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun onBackBtnClick(){
        imgBtnBack.setOnClickListener {
            findNavController().navigate(R.id.loginFragment_to_launcherFragment)
        }
    }
}