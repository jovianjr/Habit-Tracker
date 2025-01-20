package com.example.habittracker.data.repository

import com.example.habittracker.data.model.User
import com.example.habittracker.utils.UiState

interface AuthRepository {
    fun loginEmailPassword(email: String, password: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
    fun session(result: (User?) -> Unit)
}