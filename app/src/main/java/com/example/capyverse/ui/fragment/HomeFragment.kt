package com.example.capyverse.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.capyverse.databinding.FragmentHomeBinding
import com.example.capyverse.ui.activity.TaskActivity

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Navigate to FlashcardsActivity when fragmentContainer is clicked
        binding.flashcardContainer.setOnClickListener {
            val intent = Intent(requireContext(), TaskActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
}
