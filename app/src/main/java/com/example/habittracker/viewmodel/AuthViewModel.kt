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

    private val _register = MutableLiveData<UiState<String>>()
    val register: LiveData<UiState<String>>
        get() = _register

    private val _session = MutableLiveData<UiState<User?>>()
    val session: LiveData<UiState<User?>>
        get() = _session

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

    fun register(user: User) {
        _register.value = UiState.Loading
        repository.register(user){
            _register.value = it
        }
    }

    fun session() {
        _session.value = UiState.Loading
        repository.session{
            _session.value = it
        }
    }

    fun updateUser(user: User, result: (Boolean) -> Unit) {
        repository.updateUser(user, result)
    }
}
