package com.example.habittracker.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.R
import com.example.habittracker.adapter.HabitAdapter
import com.example.habittracker.adapter.HabitCompletedAdapter
import com.example.habittracker.data.model.Habit
import com.example.habittracker.databinding.FragmentDashboardBinding
import com.example.habittracker.utils.UiState
import com.example.habittracker.viewmodel.AuthViewModel
import com.example.habittracker.viewmodel.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.math.RoundingMode
import kotlin.math.roundToInt

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var myHabits: List<String>
    private val authViewModel: AuthViewModel by viewModels()
    private val habitViewModel: HabitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        authViewModel.session {
            myHabits = it?.habits?.filterNotNull() ?: emptyList()
            habitViewModel.getHabitsToday()
        }

        binding.btnHabitSettings.setOnClickListener{
            findNavController().navigate(R.id.action_dashboardFragment_to_habitSettingsFragment_navigation)
        }

        binding.ivProfileImage.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment_navigation)
        }
    }

    private fun generateRecycleViewTodo(habits: List<String?>?) {
        val data: List<String> = habits?.filterNotNull() ?: emptyList()
        binding.rvDataTodo.layoutManager = LinearLayoutManager(activity)
        binding.rvDataTodo.adapter = HabitAdapter(data)
    }

    private fun generateRecycleViewCompleted(habits: List<Habit?>?) {
        val data: List<Habit> = habits?.filterNotNull() ?: emptyList()
        binding.rvDataCompleted.layoutManager = LinearLayoutManager(activity)
        binding.rvDataCompleted.adapter = HabitCompletedAdapter(data)
    }

    private fun observer() {
        habitViewModel.habitsToday.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                }

                is UiState.Failure -> {
                }

                is UiState.Success -> {
                    generateRecycleViewCompleted(state.data)

                    // generate to do list
                    val todoList = myHabits.filterNot { myHabit ->
                        state.data.any { habit -> habit.name == myHabit }
                    }
                    generateRecycleViewTodo(todoList)

                    // update progress ui
                    val todoProgress = ((state.data.size.toDouble() / myHabits.size)*100).roundToInt()
                    binding.tvYourProgressValue.text = "$todoProgress%"
                    binding.cpiYourProgressToday.setProgress(todoProgress)
                }
            }
        }
    }

}