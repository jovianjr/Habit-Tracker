package com.example.habittracker.data.repository

import android.util.Log
import com.example.habittracker.data.model.Habit
import com.example.habittracker.data.model.User
import com.example.habittracker.utils.FireStoreCollection
import com.example.habittracker.utils.UiState
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.type.TimeOfDay
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Locale

class HabitRepositoryImp(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) :
    HabitRepository {
    override fun getHabits(user: User, result: (UiState<List<String>>) -> Unit) {
//        Log.d("HABITS WORKS", "YES IT WORKS")
    }

    override fun getHabitsToday(result: (UiState<List<Habit>>) -> Unit) {
        val today: LocalDate = LocalDate.now()
        firestore
            .collection(FireStoreCollection.HABITS)
            .document(auth.uid.toString())
            .collection(FireStoreCollection.HABITS_HISTORY)
            .document(today.toString())
            .get()
            .addOnSuccessListener { doc ->
                val dataField = doc.get("data") as? List<Map<String, Any>>
                val res = dataField?.mapNotNull { item ->
                    item["name"]?.toString()?.let { name ->
                        val updatedAt = item["updated_at"] as Timestamp
                        Habit(name, formatTimestamp(updatedAt))
                    }
                } ?: emptyList()
                result.invoke(UiState.Success(res))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Failed to fetch today's habits."))
            }
    }

    // Format the timestamp (convert to String)
    fun formatTimestamp(timestamp: com.google.firebase.Timestamp): String {
        val date = timestamp.toDate()
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        return formatter.format(date)
    }
}