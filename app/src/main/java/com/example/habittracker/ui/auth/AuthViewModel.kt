package com.example.habittracker.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.habittracker.data.model.User
import com.example.habittracker.data.repository.AuthRepository
import com.example.habittracker.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    val repository: AuthRepository
) : ViewModel() {

    private val _login = MutableLiveData<UiState<String>>()
    val login: LiveData<UiState<String>>
        get() = _login

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

    fun session(result: (User?) -> Unit){
        repository.session(result)
    }

    fun logout(result: () -> Unit){
        repository.logout(result)
    }
}
