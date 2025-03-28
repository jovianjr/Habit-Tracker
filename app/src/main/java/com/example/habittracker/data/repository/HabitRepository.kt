package com.example.habittracker.data.repository

import com.example.habittracker.data.model.Habit
import com.example.habittracker.data.model.User
import com.example.habittracker.shared.utils.UiState

interface HabitRepository {
    fun getHabits(user: User, result: (UiState<List<String>>) -> Unit)
    fun getHabitsToday(result: (UiState<List<Habit>>) -> Unit)
    fun storeHabits(newHabits: List<String>, result: (Boolean) -> Unit)
    fun storeCompleteHabit(newCompletedHabits: List<Habit>, result: (Boolean) -> Unit)
}