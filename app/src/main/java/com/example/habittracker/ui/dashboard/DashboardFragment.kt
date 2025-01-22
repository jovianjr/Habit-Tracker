package com.example.habittracker.ui.dashboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.habittracker.R
import com.example.habittracker.adapter.HabitAdapter
import com.example.habittracker.adapter.HabitCompletedAdapter
import com.example.habittracker.data.model.Habit
import com.example.habittracker.databinding.FragmentDashboardBinding
import com.example.habittracker.shared.utils.UiState
import com.example.habittracker.shared.utils.formatDate
import com.example.habittracker.viewmodel.AuthViewModel
import com.example.habittracker.viewmodel.HabitViewModel
import com.google.firebase.Timestamp
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

@AndroidEntryPoint
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding
    private lateinit var myHabits: List<String>
    private val todoHabits: MutableList<String> = mutableListOf()
    private val completedHabits: MutableList<Habit> = mutableListOf()
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
        checkSession()

        binding.tvTodayValue.text = formatDate(Calendar.getInstance().time)
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

    private fun checkSession() {
        authViewModel.session {
            myHabits = it?.habits ?: emptyList()
            if (myHabits.isEmpty()) {
                findNavController().navigate(R.id.action_dashboardFragment_to_dashboardNoDataFragment_navigation)
            } else {
                habitViewModel.getHabitsToday()
            }
        }
    }

    private fun generateRecycleViewTodo() {
        val adapter = HabitAdapter(todoHabits)
        binding.rvDataTodo.layoutManager = LinearLayoutManager(activity)
        binding.rvDataTodo.adapter = adapter

        adapter.onItemClick = {
            val fragmentManager: FragmentManager =
                (activity as FragmentActivity).supportFragmentManager
            val modal = CompleteHabitDialogFragment(it, ::storeCompletedHabit)
            modal.show(fragmentManager, CompleteHabitDialogFragment.TAG + it)
        }
    }

    private fun generateRecycleViewCompleted() {
        binding.rvDataCompleted.layoutManager = LinearLayoutManager(activity)
        binding.rvDataCompleted.adapter = HabitCompletedAdapter(completedHabits)
    }

    private fun notifyRemoveTodoHabits(habitName: String) {
        val todoHabitIdx = todoHabits.indexOf(habitName)
        todoHabits.removeAt(todoHabitIdx)
        if (todoHabits.size == 0) {
            binding.llHabitsTodo.visibility = View.GONE
        } else {
            binding.llHabitsTodo.visibility = View.VISIBLE
            binding.rvDataTodo.adapter?.notifyItemRemoved(todoHabitIdx)
            binding.rvDataTodo.adapter?.notifyItemRangeChanged(
                todoHabitIdx,
                todoHabits.size
            )
        }
    }

    private fun notifyAddCompletedHabits() {
        if (completedHabits.size == 1) {
            binding.llHabitsCompleted.visibility = View.VISIBLE
            generateRecycleViewCompleted()
        } else {
            binding.rvDataCompleted.adapter?.notifyItemInserted(completedHabits.size)
        }
    }

    private fun storeCompletedHabit(newCompletedHabitName: String, result: (Boolean) -> Unit) {
        val newCompletedHabit = Habit(newCompletedHabitName, Timestamp(Date()))
        val newCompletedHabits = completedHabits + newCompletedHabit
        habitViewModel.storeCompleteHabit(newCompletedHabits) { success ->
            result.invoke(success)
            if (success) {
                completedHabits.add(newCompletedHabit)
                notifyRemoveTodoHabits(newCompletedHabitName)
                notifyAddCompletedHabits()
                updateProgressUi()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun updateProgressUi() {
        val combinedHabits =
            (myHabits + completedHabits.map { it.name }).distinct()
        val todoProgress: Int =
            ((completedHabits.size.toDouble() / combinedHabits.size) * 100).roundToInt()
        binding.tvYourProgressValue.text = "$todoProgress%"
        binding.cpiYourProgressToday.setProgress(todoProgress.coerceAtLeast(5))
    }

    private fun observer() {
        habitViewModel.getHabitsToday.observe(viewLifecycleOwner) { state ->
            when (state) {
                is UiState.Loading -> {
                    binding.llSkeletonDataTodo.visibility = View.VISIBLE
                    binding.llSkeletonDataCompleted.visibility = View.VISIBLE
                    completedHabits.clear()
                    todoHabits.clear()
                }

                is UiState.Failure -> {
                    binding.llSkeletonDataTodo.visibility = View.GONE
                    binding.llSkeletonDataCompleted.visibility = View.GONE
                }

                is UiState.Success -> {
                    binding.llSkeletonDataTodo.visibility = View.GONE
                    binding.llSkeletonDataCompleted.visibility = View.GONE

                    // assign data
                    completedHabits.addAll(state.data)
                    todoHabits.addAll(myHabits.filterNot { habit ->
                        completedHabits.any { it.name == habit }
                    })

                    // to do data
                    generateRecycleViewTodo()

                    // completed data
                    val combinedHabits =
                        (myHabits + completedHabits.map { it.name }).distinct()
                    if (completedHabits.isEmpty()) {
                        binding.llHabitsTodo.visibility = View.VISIBLE
                        binding.llHabitsCompleted.visibility = View.GONE
                    } else if (completedHabits.size == combinedHabits.size) {
                        binding.llHabitsTodo.visibility = View.GONE
                        binding.llHabitsCompleted.visibility = View.VISIBLE
                        generateRecycleViewCompleted()
                    } else {
                        binding.llHabitsTodo.visibility = View.VISIBLE
                        binding.llHabitsCompleted.visibility = View.VISIBLE
                        generateRecycleViewCompleted()
                    }

                    // update progress ui
                    updateProgressUi()
                }
            }
        }
    }
}