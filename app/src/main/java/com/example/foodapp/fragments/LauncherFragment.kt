package com.example.foodapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.foodapp.R
import com.example.foodapp.Util
import com.example.foodapp.activities.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth


class LauncherFragment : Fragment() {
    private lateinit var btnSkip: Button
    private lateinit var btnGoogle: LinearLayout
    private lateinit var btnSignup : LinearLayout
    private lateinit var tvLogin : TextView
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_launcher, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI(view)
        auth = Firebase.auth

        btnGoogle.setOnClickListener {
            signInWithGoogle()
        }

        btnSkip.setOnClickListener {
            Util.showAlertDialog("Wait! Are you sure?",
                "You'll miss out on personalized content and saving our delicious recipes.",
                "No, Go Back",
                "Yes, I'm Sure",
                requireActivity(),
                MainActivity::class.java
            )
        }

        btnSignup.setOnClickListener{
            findNavController().navigate(R.id.launcherFragment_to_signupFragment)
        }
        tvLogin.setOnClickListener{
            findNavController().navigate(R.id.launcherFragment_to_loginFragment)
        }

    }

    private fun initUI(view : View) {
        btnSkip = view.findViewById(R.id.btnSkip)
        btnGoogle = view.findViewById(R.id.btnGoogle)
        btnSignup = view.findViewById(R.id.btnSignUp)
        tvLogin = view.findViewById(R.id.tvLogin)
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null) {
            val firstName = currentUser.displayName?.split(" ")?.get(0)
            Toast.makeText(requireActivity(), "Welcome back $firstName", Toast.LENGTH_SHORT).show()
            val intent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }
    }

    private fun signInWithGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (_: ApiException) {
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    Toast.makeText(requireActivity(), "Signed in as ${user?.displayName}", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(requireActivity(), MainActivity::class.java)
                    startActivity(intent)
                    requireActivity().finish()
                } else {
                    Toast.makeText(requireActivity(), "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}