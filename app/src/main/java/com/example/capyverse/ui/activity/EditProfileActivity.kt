package com.example.capyverse.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.capyverse.R
import com.example.capyverse.databinding.ActivityEditProfileBinding
import com.example.capyverse.viewmodel.UserViewModel
import com.example.capyverse.repository.UserRepositoryImpl
import com.google.firebase.database.FirebaseDatabase

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        userViewModel = UserViewModel(UserRepositoryImpl())

        val currentUser = userViewModel.getCurrentUser()

        // Populate fields with current user data (name and email)
        currentUser?.let {
            userViewModel.getUserFromDatabase(it.uid.toString())
        }

        userViewModel.userData.observe(this) { user ->
            binding.fullNameEditText.setText("${user?.firstName} ${user?.lastName}")
            binding.emailEditText.setText(user?.email)
        }

        binding.saveProfileBtn.setOnClickListener {
            val updatedData = mutableMapOf<String, Any>()
            val updatedName = binding.fullNameEditText.text.toString().trim()
            val updatedEmail = binding.emailEditText.text.toString().trim()

            if (updatedName.isNotEmpty() && updatedEmail.isNotEmpty()) {
                updatedData["firstName"] = updatedName.split(" ")[0]
                updatedData["lastName"] = updatedName.split(" ")[1]
                updatedData["email"] = updatedEmail

                currentUser?.let { user ->
                    // Update user profile in Firebase Realtime Database
                    updateUserProfile(user.uid.toString(), updatedData)
                }
            } else {
                Toast.makeText(this, "Please fill out both fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateUserProfile(userId: String, updatedData: MutableMap<String, Any>) {
        val userRef = FirebaseDatabase.getInstance().getReference("users").child(userId)
        userRef.updateChildren(updatedData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Profile updated successfully!", Toast.LENGTH_SHORT).show()
                    // You can navigate back to the Profile screen or finish this activity
                    finish() // Close the EditProfileActivity
                } else {
                    Toast.makeText(this, "Error updating profile: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
