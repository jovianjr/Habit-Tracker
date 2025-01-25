package com.example.habittracker.data.model

data class User(
    val uid: String? = "",
    var name: String? = "",
    var email: String? = "",
    var profileImage: String? = "",
    var noteToSelf: String? = "",
    var habits: List<String>? = emptyList(),
    var password: String? = "",
)