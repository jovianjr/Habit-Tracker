package com.example.habittracker.shared.utils

import com.example.habittracker.R
import com.example.habittracker.data.model.RegisterStep

object FireStoreCollection {
    const val HABITS = "habits"
    const val HABITS_HISTORY = "habits_history"
}

object SharedPrefConstants {
    const val LOCAL_SHARED_PREF = "local_shared_pref"
    const val USER_SESSION = "user_session"
}

object ProfileImageListConstants {
    val images = mapOf(
        "default" to R.drawable.img_photo_profile_6,
        "img_photo_profile_1" to R.drawable.img_photo_profile_1,
        "img_photo_profile_2" to R.drawable.img_photo_profile_2,
        "img_photo_profile_3" to R.drawable.img_photo_profile_3,
        "img_photo_profile_4" to R.drawable.img_photo_profile_4,
        "img_photo_profile_5" to R.drawable.img_photo_profile_5,
        "img_photo_profile_7" to R.drawable.img_photo_profile_7,
        "img_photo_profile_8" to R.drawable.img_photo_profile_8,
    )
}

object RegisterStepConstants {
    val steps = listOf(
        RegisterStep(1, "Complete Your Info"),
        RegisterStep(2, "Terms & Conditions"),
        RegisterStep(3, "You’re All Set!"),
    )
}