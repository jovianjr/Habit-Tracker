package com.example.habittracker.data.repository

import android.content.SharedPreferences
import com.example.habittracker.data.model.User
import com.example.habittracker.shared.utils.FireStoreCollection
import com.example.habittracker.shared.utils.SharedPrefConstants
import com.example.habittracker.shared.utils.UiState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.gson.Gson

class AuthRepositoryImp(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore,
    private val gson: Gson,
    private val sharedPreferences: SharedPreferences,
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
                        profileImage = res?.profileImage
                            ?: if (user.photoUrl != null) user.photoUrl.toString() else "placeholder",
                        noteToSelf = res?.noteToSelf,
                        habits = res?.habits
                    )
                    sharedPreferences.edit()
                        .putString(SharedPrefConstants.USER_SESSION, gson.toJson(userData)).apply()
                    result.invoke(userData)
                }
                .addOnFailureListener {
                    result.invoke(null)
                }

        }
    }

    override fun getUser(result: (User?) -> Unit) {
        val user = sharedPreferences.getString(SharedPrefConstants.USER_SESSION, null)
        if (user == null) {
            result.invoke(null)
        } else {
            val userData = gson.fromJson(user, User::class.java)
            result.invoke(userData)
        }
    }

    override fun updateUser(user: User, result: (Boolean) -> Unit) {
        val docData = hashMapOf(
            "profileImage" to user.profileImage,
            "name" to user.name,
            "noteToSelf" to user.noteToSelf,
        )

        firestore
            .collection(FireStoreCollection.HABITS)
            .document(auth.uid.toString())
            .set(docData, SetOptions.mergeFields("profileImage", "name", "noteToSelf"))
            .addOnSuccessListener {
                result.invoke(true)
                sharedPreferences.edit()
                    .putString(SharedPrefConstants.USER_SESSION, gson.toJson(user)).apply()

            }
            .addOnFailureListener {
                result.invoke(false)
            }
    }
}