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
import com.example.habittracker.shared.utils.UiState
import com.example.habittracker.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeScreenFragment : Fragment() {
    private lateinit var binding: FragmentWelcomeScreenBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWelcomeScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()
        authViewModel.session()

        // btn handler
        binding.btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeScreenFragment_to_loginFragment_navigation)
        }
        binding.btnRegister.setOnClickListener {
            findNavController().navigate(R.id.action_welcomeScreenFragment_to_registerFragment_navigation)
        }

    }

    private fun observer() {
        authViewModel.session.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Failure -> {
                }

                is UiState.Success -> {
                    val myHabits: List<String> = state.data?.habits ?: emptyList()
                    if (myHabits.isEmpty()) {
                        findNavController().navigate(R.id.action_welcomeScreenFragment_to_dashboardNoDataFragment_navigation)
                    } else {
                        findNavController().navigate(R.id.action_welcomeScreenFragment_to_dashboardFragment_navigation)
                    }
                }
            }
        }
    }

}