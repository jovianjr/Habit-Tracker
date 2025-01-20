package com.example.habittracker.data.repository

import android.util.Log
import com.example.habittracker.data.model.User
import com.example.habittracker.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson

class AuthRepositoryImp(
    private val auth: FirebaseAuth,
    private val gson: Gson
) : AuthRepository {
    override fun loginEmailPassword(
        email: String,
        password: String,
        result: (UiState<String>) -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    result.invoke(UiState.Success("Login successfully"))
                } else {
                    result.invoke(UiState.Failure("Authentication failed"))
                }
            }
    }

    override fun logout(result: () -> Unit) {
        auth.signOut()
        result.invoke()
    }

    override fun session(result: (User?) -> Unit) {
        val user = auth.currentUser
        if (user == null) {
            result.invoke(null)
        } else {
            val user = User(
                id = user.uid,
                name = user.displayName ?: "BOT John Doe",
                email = user.email,
                profileImage = if (user.photoUrl != null) user.photoUrl.toString() else "placeholder"
            )
            result.invoke(user)
        }
    }
}