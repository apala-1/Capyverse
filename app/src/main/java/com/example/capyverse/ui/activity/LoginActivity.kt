package com.example.capyverse.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capyverse.R
import com.example.capyverse.databinding.ActivityLoginBinding
import com.example.capyverse.ui.fragment.ProfileFragment
import com.example.capyverse.utils.LoadingUtils
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    lateinit var  loadingUtils: LoadingUtils
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.loginButton.setOnClickListener {
            val email = binding.email.text.toString()
            val password = binding.password.text.toString()

            if(email.isNotEmpty()&&password.isNotEmpty()){
                if(android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    loadingUtils.show()
                    firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener { task ->
                        loadingUtils.dismiss()
                        if (task.isSuccessful) {
                            Toast.makeText(
                                this@LoginActivity,
                                "Login Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            navigateToIndex()
                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Error: ${task.exception?.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }else{
                    Toast.makeText(
                        this@LoginActivity,
                        "Please enter a valid email address",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }else{
                Toast.makeText(
                    this@LoginActivity,
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }

        binding.backBtn.setOnClickListener{
            val intent = Intent(
                this@LoginActivity,
                HomePageActivity::class.java
            )
            startActivity(intent)
        }

        binding.forgotPassBtn.setOnClickListener {
            val intent = Intent(
                this@LoginActivity,
                ForgotPasswordActivity::class.java
            )
            startActivity(intent)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private  fun navigateToIndex(){
        val intent = Intent(this@LoginActivity,IndexPageActivity::class.java)
        startActivity(intent)
    }
}