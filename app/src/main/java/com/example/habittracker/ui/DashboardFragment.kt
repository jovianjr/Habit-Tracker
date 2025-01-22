package com.example.habittracker.ui

import android.annotation.SuppressLint
import android.os.Bundle
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
import com.example.habittracker.utils.formatDate
import com.example.habittracker.viewmodel.AuthViewModel
import com.example.habittracker.viewmodel.HabitViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import kotlin.math.roundToInt

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var myHabits: List<String>
    private val authViewModel: AuthViewModel by viewModels()
    private val habitViewModel: HabitViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDashboardBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observer()

        binding.tvTodayValue.text = formatDate(Calendar.getInstance().time)
        authViewModel.session {
            myHabits = it?.habits ?: emptyList()

            if (myHabits.isEmpty()) {
                findNavController().navigate(R.id.action_dashboardFragment_to_dashboardNoDataFragment_navigation)
            } else {
                habitViewModel.getHabitsToday()
            }
        }
        binding.btnHabitSettings.setOnClickListener {
            val bundle = Bundle()
            bundle.putStringArrayList("myHabits", ArrayList(myHabits))
            findNavController().navigate(
                R.id.action_dashboardFragment_to_habitSettingsFragment_navigation,
                bundle
            )
        }

        binding.ivProfileImage.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_profileFragment_navigation)
        }
    }

    private fun generateRecycleViewTodo(habits: List<String>?) {
        val data: List<String> = habits ?: emptyList()
        binding.rvDataTodo.layoutManager = LinearLayoutManager(activity)
        binding.rvDataTodo.adapter = HabitAdapter(data)
    }

    private fun generateRecycleViewCompleted(habits: List<Habit?>?) {
        val data: List<Habit> = habits?.filterNotNull() ?: emptyList()
        binding.rvDataCompleted.layoutManager = LinearLayoutManager(activity)
        binding.rvDataCompleted.adapter = HabitCompletedAdapter(data)
    }

    @SuppressLint("SetTextI18n")
    private fun observer() {
        habitViewModel.habitsToday.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.llSkeletonDataTodo.visibility = View.VISIBLE
                    binding.llSkeletonDataCompleted.visibility = View.VISIBLE
                }

                is UiState.Failure -> {
                    binding.llSkeletonDataTodo.visibility = View.GONE
                    binding.llSkeletonDataCompleted.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.llSkeletonDataTodo.visibility = View.GONE
                    binding.llSkeletonDataCompleted.visibility = View.GONE

                    // generate to do list
                    val todoList: List<String> = myHabits.filterNot { myHabit ->
                        state.data.any { habit -> habit.name == myHabit }
                    }
                    generateRecycleViewTodo(todoList)

                    // completed data
                    val combinedHabits = (myHabits.toList() + state.data.map { it.name }).distinct()
                    if (state.data.isEmpty()) {
                        binding.llHabitsTodo.visibility = View.VISIBLE
                        binding.llHabitsCompleted.visibility = View.GONE
                    } else if (state.data.size == combinedHabits.size) {
                        binding.llHabitsTodo.visibility = View.GONE
                        binding.llHabitsCompleted.visibility = View.VISIBLE
                        generateRecycleViewCompleted(state.data)
                    } else {
                        binding.llHabitsTodo.visibility = View.VISIBLE
                        binding.llHabitsCompleted.visibility = View.VISIBLE
                        generateRecycleViewCompleted(state.data)
                    }

                    // update progress ui
                    val todoProgress: Int =
                        ((state.data.size.toDouble() / combinedHabits.size) * 100).roundToInt()
                    binding.tvYourProgressValue.text = "$todoProgress%"
                    binding.cpiYourProgressToday.setProgress(todoProgress.coerceAtLeast(5))
                }
            }
        }
    }

}