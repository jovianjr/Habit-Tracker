package com.example.habittracker.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.data.model.Habit
import com.example.habittracker.shared.utils.formatDate

class HabitCompletedAdapter(private val items: List<Habit>) :
    RecyclerView.Adapter<HabitCompletedAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvHabitName: TextView = itemView.findViewById(R.id.tv_habit_name)
        val tvHabitTime: TextView = itemView.findViewById(R.id.tv_habit_time)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view =
            LayoutInflater.from(parent.context)
                .inflate(R.layout.rv_habit_card_completed, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvHabitName.text = items[position].name

        if (items[position].updatedAt != null) {
            holder.tvHabitTime.text = formatDate(items[position].updatedAt!!.toDate(), "HH:mm")
        }
    }

    override fun getItemCount(): Int = items.size

}