package com.example.habittracker

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContentView(R.layout.splash_screen)

//        setContent {
//            HabitTrackerTheme {
//                Surface(
//                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
//                ) {
//                    setContentView(R.layout.splash_screen)
//                }
//            }
//        }
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        Log.d("user", currentUser.toString())
        if (currentUser != null) {
            setContentView(R.layout.dashboard)
            generateRecycleViewTodo()
            generateRecycleViewCompleted()
        } else {
            setContentView(R.layout.welcome_screen)

            // btn handler
            val btnLogin = findViewById<Button>(R.id.btn_login)
            val btnRegister = findViewById<Button>(R.id.btn_register)
            btnLogin.setOnClickListener {
                startActivity(Intent(this, AuthLoginActivity::class.java))
            }
            btnRegister.setOnClickListener {
                startActivity(Intent(this, AuthRegisterActivity::class.java))
            }
        }
    }



    fun generateRecycleViewTodo() {
        // Data untuk RecyclerView
        val data = List(2) { "Item ${it + 1}" }

        // Temukan RecyclerView di layout
        val recyclerView: RecyclerView = findViewById(R.id.rv_data_todo)

        // Atur layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Atur adapter
        recyclerView.adapter = MyAdapter(data)
    }

    fun generateRecycleViewCompleted() {
        // Data untuk RecyclerView
        val data = List(1) { "Item ${it + 1}" }

        // Temukan RecyclerView di layout
        val recyclerView: RecyclerView = findViewById(R.id.rv_data_completed)

        // Atur layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Atur adapter
        recyclerView.adapter = MyAdapter(data)
    }
}
