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
    lateinit var binding: ActivitySignUpBinding
    lateinit var userViewModel: UserViewModel
    lateinit var loadingUtils: LoadingUtils
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadingUtils = LoadingUtils(this)

        val userRepository = UserRepositoryImpl()

        userViewModel = UserViewModel(userRepository)

        binding.alreadyHaveAccount.setOnClickListener {
            val intent = Intent(this@SignUpActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.backBtn.setOnClickListener{
            val intent = Intent(
                this@SignUpActivity,
                HomePageActivity::class.java
            )
            startActivity(intent)
        }

        binding.signUpButton.setOnClickListener {
            loadingUtils.show()
            var email: String = binding.email.text.toString()
            var password: String = binding.password.text.toString()
            var firstName: String = binding.firstName.text.toString()
            var lastName: String = binding.lastName.text.toString()
            userViewModel.signup(email,password){
                success,message,userId ->
                if(success){
                    var userModel = UserModel(
                        userId, email, firstName, lastName
                    )
                    addUser(userModel)
                }else{
                    loadingUtils.dismiss()
                    Toast.makeText(this@SignUpActivity,
                        message, Toast.LENGTH_SHORT).show()
                }
            }

        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    fun addUser(userModel: UserModel){
        userViewModel.addUserToDatabase(userModel.userId,userModel){
            success, message ->
            if(success){
                loadingUtils.dismiss()
                Toast.makeText(this@SignUpActivity,
                    message,Toast.LENGTH_SHORT).show()
            }else{
                loadingUtils.dismiss()
                Toast.makeText(this@SignUpActivity,
                    message,Toast.LENGTH_SHORT).show()
            }
        }
    }
}