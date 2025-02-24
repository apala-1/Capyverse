package com.example.capyverse.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.capyverse.R
import com.example.capyverse.databinding.ActivitySignUpBinding
import com.example.capyverse.model.UserModel
import com.example.capyverse.repository.UserRepositoryImpl
import com.example.capyverse.utils.LoadingUtils
import com.example.capyverse.viewmodel.UserViewModel

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var userViewModel: UserViewModel
    private lateinit var loadingUtils: LoadingUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)
        userViewModel = UserViewModel(UserRepositoryImpl())

        setupClickListeners()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setupClickListeners() {
        binding.alreadyHaveAccount.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.backBtn.setOnClickListener {
            startActivity(Intent(this, HomePageActivity::class.java))
            finish()
        }

        binding.signUpButton.setOnClickListener {
            handleSignUp()
        }
    }

    private fun handleSignUp() {
        val email = binding.email.text.toString().trim()
        val password = binding.password.text.toString().trim()
        val firstName = binding.firstName.text.toString().trim()
        val lastName = binding.lastName.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || firstName.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidEmail(email)) {
            Toast.makeText(this, "Please enter a valid email address.", Toast.LENGTH_SHORT).show()
            return
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long.", Toast.LENGTH_SHORT).show()
            return
        }

        loadingUtils.show()

        userViewModel.signup(email, password) { success, message, userId ->
            loadingUtils.dismiss()
            if (success && userId != null) {
                val userModel = UserModel(userId, email, firstName, lastName)
                addUser(userModel)
            } else {
                Toast.makeText(this, message ?: "Sign-up failed. Please try again.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+"
        return email.matches(emailRegex.toRegex())
    }


    private fun addUser(userModel: UserModel) {
        userViewModel.addUserToDatabase(userModel.userId, userModel) { success, message ->
            loadingUtils.dismiss()
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            if (success) {
                startActivity(Intent(this, HomePageActivity::class.java))
                finish()
            }
        }
    }
}
