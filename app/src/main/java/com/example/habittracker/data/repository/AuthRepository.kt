package com.example.habittracker.data.repository

import com.example.habittracker.data.model.User
import com.example.habittracker.shared.utils.UiState

interface AuthRepository {
    fun getUser(result: (User?) -> Unit)
    fun loginEmailPassword(email: String, password: String, result: (UiState<String>) -> Unit)
    fun logout(result: () -> Unit)
    fun register(user: User, result: (UiState<String>) -> Unit)
    fun session(result: (UiState<User?>) -> Unit)
    fun updateUser(user: User, result: (Boolean) -> Unit)
}