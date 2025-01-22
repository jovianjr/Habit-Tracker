package com.example.habittracker.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.google.android.material.card.MaterialCardView


class HabitAdapter(private val items: List<String>) :
    RecyclerView.Adapter<HabitAdapter.ViewHolder>() {
    var onItemClick: ((String) -> Unit)? = null

    // ViewHolder untuk item RecyclerView
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.tv_habit_name)
        val materialCardView: MaterialCardView = itemView.findViewById(R.id.cv_habit)
    }

    // Inflate layout untuk setiap item
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_habit_card, parent, false)
        return ViewHolder(view)
    }

    // Bind data ke item
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView.text = items[position]

        // set color
        val colorPalette = listOf(
            Color.parseColor("#FFFEF3C7"),
            Color.parseColor("#FFDBEAFE"),
            Color.parseColor("#FFF4F4F5"),
            Color.parseColor("#FFEDE9FE"),
            Color.parseColor("#FFFFE4E6"),
        )
        val randomBackgroundColor = ColorStateList.valueOf(colorPalette.random())
        holder.materialCardView.setCardBackgroundColor(randomBackgroundColor)

        // on card click
        holder.materialCardView.setOnClickListener {
            onItemClick?.invoke(items[position])
        }
    }

    // Total jumlah item
    override fun getItemCount(): Int = items.size
}