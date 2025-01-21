package com.example.habittracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentWelcomeScreenBinding
import com.example.habittracker.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeScreenFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeScreenBinding
    private val viewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.session { user ->
            if (user != null){
                findNavController().navigate(R.id.action_welcomeScreenFragment_to_dashboardFragment_navigation)
            }
        }

        // btn handler
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeScreenFragment_to_loginFragment_navigation)
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeScreenFragment_to_registerFragment_navigation)
        }

    }

}