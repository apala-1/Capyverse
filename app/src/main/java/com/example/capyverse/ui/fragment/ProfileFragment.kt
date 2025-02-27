package com.example.capyverse.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.capyverse.R
import com.example.capyverse.databinding.FragmentProfileBinding
import com.example.capyverse.repository.UserRepositoryImpl
import com.example.capyverse.ui.activity.EditProfileActivity
import com.example.capyverse.viewmodel.UserViewModel
import com.example.capyverse.ui.activity.LoginActivity

class ProfileFragment : Fragment() {
    lateinit var binding: FragmentProfileBinding
    lateinit var userViewModel: UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repo = UserRepositoryImpl()
        userViewModel = UserViewModel(repo)

        val currentUser = userViewModel.getCurrentUser()

        currentUser?.let {
            // Fetch user data from the database and observe it
            userViewModel.getUserFromDatabase(it.uid.toString())
        }

        userViewModel.userData.observe(viewLifecycleOwner) { users ->
            binding.email.text = users?.email
            binding.fullName.text = "${users?.firstName} ${users?.lastName}"

        }

        // Set click listener for navigating to the EditProfileFragment
        binding.editProfBtn.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        // Log out button functionality
        binding.logOutBtn.setOnClickListener {
            userViewModel.logout { success, message ->
                if (success) {
                    Toast.makeText(requireContext(), "Logged out successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(requireContext(), LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    Toast.makeText(requireContext(), "Error: $message", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}