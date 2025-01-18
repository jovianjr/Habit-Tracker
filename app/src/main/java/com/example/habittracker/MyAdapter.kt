package com.example.habittracker

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlin.random.Random

class MyAdapter(private val items: List<String>) : RecyclerView.Adapter<MyAdapter.ViewHolder>() {

    // ViewHolder untuk item RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_habit_name)
        val materialCardView: MaterialCardView = itemView.findViewById(R.id.cv_habit)
    }

    // Inflate layout untuk setiap item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_habit_card, parent, false)
        return ViewHolder(view)
    }

    // Bind data ke item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]

// Generate random color
        val randomBackgroundColor = Color.rgb(
            Random.nextInt(256),  // Random red value
            Random.nextInt(256),  // Random green value
            Random.nextInt(256)   // Random blue value
        )
        holder.materialCardView.setCardBackgroundColor(randomBackgroundColor)

    }

    // Total jumlah item
    override fun getItemCount(): Int = items.size
}