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
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.activities.MainActivity
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
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
        etEmail = view.findViewById(R.id.etEmail)
        etPass = view.findViewById(R.id.etPass)
        btnStartCooking = view.findViewById(R.id.btnStartCooking)
    }

    private fun onStartCookingBtnClick() {
        btnStartCooking.setOnClickListener {
            val email = etEmail.text.toString().replace("\\s".toRegex(), "")
            val pass = etPass.text.toString()
            if (email.isEmpty()) {
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
                    val currentUser = auth.currentUser
                    Toast.makeText(requireActivity(), "Signed in as ${currentUser?.displayName}", Toast.LENGTH_SHORT).show()
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