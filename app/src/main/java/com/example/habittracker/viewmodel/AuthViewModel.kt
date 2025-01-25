package com.example.habittracker.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.data.model.User
import com.example.habittracker.data.repository.AuthRepository
import com.example.habittracker.shared.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repository: AuthRepository
) : ViewModel() {

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

    fun getUser(result: (User?) -> Unit) {
        repository.getUser(result)
    }

    fun login(
        email: String,
        password: String
    ) {
        _login.value = UiState.Loading
        repository.loginEmailPassword(
            email,
            password
        ) {
            _login.value = it
        }
    }

    fun logout(result: () -> Unit) {
        repository.logout(result)
    }

    fun register(user: User, result: (Boolean) -> Unit) {
        repository.register(user, result)
    }

    fun session(result: (User?) -> Unit) {
        repository.session(result)
    }

    fun updateUser(user: User, result: (Boolean) -> Unit) {
        repository.updateUser(user, result)
    }
}
