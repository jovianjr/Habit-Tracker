package com.example.habittracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.data.model.Habit
import com.example.habittracker.data.repository.HabitRepository
import com.example.habittracker.shared.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    val repository: HabitRepository
) : ViewModel() {
    private val _getHabitsToday = MutableLiveData<UiState<List<Habit>>>()
    val getHabitsToday: LiveData<UiState<List<Habit>>>
        get() = _getHabitsToday

    fun getHabitsToday() {
        _getHabitsToday.value = UiState.Loading
        repository.getHabitsToday {
            _getHabitsToday.value = it
        }
    }

    fun storeCompleteHabit(habits: List<Habit>, result: (Boolean) -> Unit) {
        repository.storeCompleteHabit(habits, result)
    }
}