package com.example.habittracker.data.model

data class User(
    var uid: String? = "",
    val name: String? = "",
    val email: String? = "",
    val profileImage: String? = "",
    val habits: List<String>? = emptyList(),
)