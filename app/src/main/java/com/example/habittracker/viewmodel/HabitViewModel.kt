package com.example.habittracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.data.model.Habit
import com.example.habittracker.data.model.User
import com.example.habittracker.data.repository.HabitRepository
import com.example.habittracker.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HabitViewModel @Inject constructor(
    val repository: HabitRepository
) : ViewModel() {
    private val _habits = MutableLiveData<UiState<List<String>>>()
    val habits: LiveData<UiState<List<String>>>
        get() = _habits

    private val _habitsToday = MutableLiveData<UiState<List<Habit>>>()
    val habitsToday: LiveData<UiState<List<Habit>>>
        get() = _habitsToday

    fun getHabits(user: User) {
        _habits.value = UiState.Loading
        repository.getHabits(user) {
            _habits.value = it
        }
    }

    fun getHabitsToday() {
        _habitsToday.value = UiState.Loading
        repository.getHabitsToday() {
            _habitsToday.value = it
        }
    }
}