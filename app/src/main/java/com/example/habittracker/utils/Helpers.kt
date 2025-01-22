package com.example.habittracker.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun formatDate(date: Date, format: String = "EEEE, dd MMMM yyyy"): String {
    return SimpleDateFormat(format, Locale.ENGLISH).format(date)
}