package com.example.habittracker.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentSplashScreenBinding
import com.example.habittracker.shared.utils.UiState
import com.example.habittracker.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        observer()
        authViewModel.session()
    }

    private fun observer() {
        authViewModel.session.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Failure -> {
                    findNavController().navigate(R.id.action_splashScreenFragment_to_welcomeScreenFragment_navigation)
                }

                is UiState.Success -> {
                    val myHabits: List<String> = state.data?.habits ?: emptyList()
                    if (myHabits.isEmpty()) {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_dashboardNoDataFragment_navigation)
                    } else {
                        findNavController().navigate(R.id.action_splashScreenFragment_to_dashboardFragment_navigation)
                    }
                }
            }
        }
    }
}