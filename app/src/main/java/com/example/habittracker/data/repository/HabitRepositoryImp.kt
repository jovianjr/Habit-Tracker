package com.example.habittracker.data.repository

import com.example.habittracker.data.model.Habit
import com.example.habittracker.data.model.User
import com.example.habittracker.utils.FireStoreCollection
import com.example.habittracker.utils.UiState
import com.example.habittracker.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.Calendar

class HabitRepositoryImp(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) :
    HabitRepository {
    override fun getHabits(user: User, result: (UiState<List<String>>) -> Unit) {
//        Log.d("HABITS WORKS", "YES IT WORKS")
    }

    override fun getHabitsToday(result: (UiState<List<Habit>>) -> Unit) {
        val today = formatDate(Calendar.getInstance().time, "YYYY-MM-DD")
        firestore
            .collection(FireStoreCollection.HABITS)
            .document(auth.uid.toString())
            .collection(FireStoreCollection.HABITS_HISTORY)
            .document(today)
            .get()
            .addOnSuccessListener { doc ->
                val dataField = doc.get("data") as? List<Map<String, Any>>
                val res = dataField?.mapNotNull { item ->
                    item["name"]?.toString()?.let { name ->
                        val updatedAt = item["updated_at"] as Timestamp
                        Habit(name, formatDate(updatedAt.toDate(), "HH:mm"))
                    }
                } ?: emptyList()
                result.invoke(UiState.Success(res))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Failed to fetch today's habits."))
            }
    }
}