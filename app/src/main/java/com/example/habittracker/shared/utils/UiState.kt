package com.example.habittracker.shared.utils

sealed class UiState<out T> {
    data object Loading: UiState<Nothing>()
    data class Success<out T>(val data: T): UiState<T>()
    data class Failure(val error: String?): UiState<Nothing>()
}