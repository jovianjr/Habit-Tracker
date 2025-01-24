package com.example.habittracker.di

import android.content.SharedPreferences
import com.example.habittracker.data.repository.AuthRepository
import com.example.habittracker.data.repository.AuthRepositoryImp
import com.example.habittracker.data.repository.HabitRepository
import com.example.habittracker.data.repository.HabitRepositoryImp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.gson.Gson
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
object RepositoryModule {
    @Provides
    @Singleton
    fun provideAuthRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore,
        gson: Gson,
        sharedPreferences: SharedPreferences
    ): AuthRepository {
        return AuthRepositoryImp(auth, firestore, gson, sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideHabitRepository(
        auth: FirebaseAuth,
        firestore: FirebaseFirestore
    ): HabitRepository {
        return HabitRepositoryImp(auth, firestore)
    }
}