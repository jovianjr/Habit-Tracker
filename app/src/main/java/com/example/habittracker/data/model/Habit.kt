package com.example.habittracker.data.model

import com.google.firebase.Timestamp

data class Habit(
    var name: String = "",
    var updatedAt: Timestamp?,
)