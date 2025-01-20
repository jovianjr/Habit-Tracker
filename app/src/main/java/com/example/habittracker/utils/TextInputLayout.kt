package com.example.habittracker.utils
import com.google.android.material.textfield.TextInputLayout

val TextInputLayout.textValue: String
    get() = this.editText?.text.toString()

