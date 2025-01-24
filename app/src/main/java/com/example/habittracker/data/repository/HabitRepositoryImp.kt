package com.example.habittracker.data.repository

import com.example.habittracker.data.model.Habit
import com.example.habittracker.data.model.User
import com.example.habittracker.shared.utils.FireStoreCollection
import com.example.habittracker.shared.utils.UiState
import com.example.habittracker.shared.utils.formatDate
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import java.util.Calendar
import java.util.Date

class HabitRepositoryImp(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) :
    HabitRepository {
    override fun getHabits(user: User, result: (UiState<List<String>>) -> Unit) {
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
                val res = dataField?.map { item ->
                    val habitName = item["name"].toString()
                    val habitTime = item["updatedAt"] as Timestamp?
                    Habit(habitName, habitTime)
                } ?: emptyList()
                result.invoke(UiState.Success(res))
            }
            .addOnFailureListener {
                result.invoke(UiState.Failure("Failed to fetch today's habits."))
            }
    }

    override fun storeHabits(newHabits: List<String>, result: (Boolean) -> Unit) {
        val docData = hashMapOf(
            "habits" to newHabits,
        )

        firestore
            .collection(FireStoreCollection.HABITS)
            .document(auth.uid.toString())
            .set(docData, SetOptions.mergeFields("habits"))
            .addOnSuccessListener {
                result.invoke(true)
            }
            .addOnFailureListener {
                result.invoke(false)
            }
    }

    override fun storeCompleteHabit(newCompletedHabits: List<Habit>, result: (Boolean) -> Unit) {
        val today = formatDate(Calendar.getInstance().time, "YYYY-MM-DD")
        val currentTimestamp = Timestamp(Date())

        val docData = hashMapOf(
            "data" to newCompletedHabits,
            "updatedAt" to currentTimestamp,
        )

        firestore
            .collection(FireStoreCollection.HABITS)
            .document(auth.uid.toString())
            .collection(FireStoreCollection.HABITS_HISTORY)
            .document(today)
            .set(docData, SetOptions.mergeFields("data", "updatedAt"))
            .addOnSuccessListener {
                result.invoke(true)
            }
            .addOnFailureListener {
                result.invoke(false)
            }
    }
}