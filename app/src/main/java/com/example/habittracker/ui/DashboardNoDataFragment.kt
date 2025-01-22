package com.example.habittracker.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentDashboardNoDataBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardNoDataFragment : Fragment() {
    private lateinit var binding: FragmentDashboardNoDataBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardNoDataBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnHabitSettings.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardNoDataFragment_to_habitSettingsFragment_navigation)
        }
    }
}