package com.example.habittracker.data.repository

import com.example.habittracker.data.model.Habit
import com.example.habittracker.data.model.User
import com.example.habittracker.utils.UiState

interface HabitRepository {
    fun getHabits(user: User, result: (UiState<List<String>>) -> Unit)
    fun getHabitsToday(result: (UiState<List<Habit>>) -> Unit)
//    fun getHabitsHistory(user: User, result: (UiState<List<Habit>>) -> Unit)
}