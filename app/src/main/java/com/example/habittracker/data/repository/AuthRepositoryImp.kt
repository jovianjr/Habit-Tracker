package com.example.habittracker.data.repository

import android.util.Log
import com.example.habittracker.data.model.User
import com.example.habittracker.utils.FireStoreCollection
import com.example.habittracker.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson

class AuthRepositoryImp(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
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
            firestore
                .collection(FireStoreCollection.HABITS)
                .document(user.uid)
                .get()
                .addOnSuccessListener {
                    val res = it.toObject(User::class.java)
                    val userData = User(
                        uid = res?.uid ?: user.uid,
                        name = res?.name ?: user.displayName,
                        email = res?.email ?: user.email,
                        profileImage = res?.profileImage ?: if (user.photoUrl != null) user.photoUrl.toString() else "placeholder",
                        habits = res?.habits
                    )
                    result.invoke(userData)
                }
                .addOnFailureListener {
                    result.invoke(null)
                }

        }
    }
}