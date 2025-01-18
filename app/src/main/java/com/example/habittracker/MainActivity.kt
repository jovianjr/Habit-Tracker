package com.example.habittracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.habittracker.ui.theme.HabitTrackerTheme
import android.widget.ListView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.LinearLayoutManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HabitTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    setContentView(R.layout.dashboard)
                    generateRecycleViewTodo()
                }
            }
        }
    }

    fun generateRecycleViewTodo(){
        // Data untuk RecyclerView
        val data = List(50) { "Item ${it + 1}" }

        // Temukan RecyclerView di layout
        val recyclerView: RecyclerView = findViewById(R.id.rv_data_todo)

        // Atur layout manager
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Atur adapter
        recyclerView.adapter = MyAdapter(data)
    }
}
