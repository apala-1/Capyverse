package com.example.capyverse.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capyverse.R
import com.example.capyverse.databinding.ActivityForgotPasswordBinding
import com.example.capyverse.utils.LoadingUtils
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityForgotPasswordBinding
    lateinit var loadingUtils: LoadingUtils
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)
        firebaseAuth = FirebaseAuth.getInstance()

        binding.backBtn.setOnClickListener{
            val intent = Intent(
                this@ForgotPasswordActivity,
                LoginActivity::class.java
            )
            startActivity(intent)
        }

        binding.resetButton.setOnClickListener {
            val email = binding.email.text.toString()

            if (email.isNotEmpty()){
                loadingUtils.show()
                firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener { task ->
                    loadingUtils.dismiss()
                    if (task.isSuccessful){
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Reset link sent to your email",
                            Toast.LENGTH_SHORT
                        ).show()
                    }else{
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Error: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }else{
                Toast.makeText(
                    this@ForgotPasswordActivity,
                    "Please fill in all the fields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}