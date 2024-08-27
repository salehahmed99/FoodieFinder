package com.example.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.activities.MainActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth



class SignupFragment : Fragment() {
    private lateinit var imgBtnBack : ImageButton
    private lateinit var etDisplayName : TextInputEditText
    private lateinit var etEmail : TextInputEditText
    private lateinit var etPass : TextInputEditText
    private lateinit var btnStartCooking : Button
    private lateinit var auth : FirebaseAuth
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_signup, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        auth = Firebase.auth
        onStartCookingBtnClick()
        onBackBtnClick()
    }

    private fun initUI(view: View) {
        imgBtnBack = view.findViewById(R.id.imgBtnBack)
        etDisplayName = view.findViewById(R.id.etDisplayName)
        etEmail = view.findViewById(R.id.etEmail)
        etPass = view.findViewById(R.id.etPass)
        btnStartCooking = view.findViewById(R.id.btnStartCooking)
    }

    private fun onStartCookingBtnClick() {
        btnStartCooking.setOnClickListener {
            val displayName = etDisplayName.text.toString()
            val email = etEmail.text.toString().replace("\\s".toRegex(), "")
            val pass = etPass.text.toString()
            if (displayName.isBlank()){
                Toast.makeText(activity, "Enter your Name!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (email.isEmpty()) {
                Toast.makeText(activity, "Enter your Email!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if (pass.isBlank()) {
                Toast.makeText(activity, "Enter your Password!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            registerWithFirebase(displayName , email , pass)
        }
    }

    private fun onBackBtnClick(){
        imgBtnBack.setOnClickListener {
            findNavController().navigate(R.id.signupFragment_to_launcherFragment)
        }
    }

    private fun registerWithFirebase(displayName : String, email : String, pass : String){
        auth.createUserWithEmailAndPassword(email, pass)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(displayName)
                        .build()
                    currentUser?.updateProfile(profileUpdates)
                    Toast.makeText(requireActivity(), "Signed in as $displayName", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()

                } else {
                    Toast.makeText(activity,"Invalid Email or Password Format", Toast.LENGTH_SHORT).show()
                }
            }
    }
}