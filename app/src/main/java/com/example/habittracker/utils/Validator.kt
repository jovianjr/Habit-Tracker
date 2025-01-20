package com.example.habittracker.utils

import android.util.Patterns
import java.util.regex.Matcher
import java.util.regex.Pattern

object Validator {
    fun isEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    fun isPassword(password: String): Boolean {
        val pattern =
            Pattern.compile("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+=])(?=\\S+$).{4,}$")
        val matcher: Matcher = pattern.matcher(password)
        return matcher.matches()
    }
}