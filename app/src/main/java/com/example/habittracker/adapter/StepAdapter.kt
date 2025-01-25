package com.example.habittracker.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.data.model.RegisterStep

class StepAdapter(private val items: List<RegisterStep>) :
    RecyclerView.Adapter<StepAdapter.ViewHolder>() {
    var currentStep: Int = 1

    fun nextStep() {
        currentStep += 1
        notifyItemChanged(currentStep - 1)
    }

    fun previousStep(){
        currentStep -= 1
        notifyItemChanged(currentStep - 1)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvCounter: TextView = itemView.findViewById(R.id.tv_counter)
        val tvName: TextView = itemView.findViewById(R.id.tv_name)
        val tvNextIndicator: TextView = itemView.findViewById(R.id.tv_next_indicator)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_step, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.tvCounter.text = items[position].number.toString()
        holder.tvName.text = items[position].name

        val context = holder.itemView.context.resources
        if (items[position].number <= currentStep) {
            holder.tvCounter.setBackgroundResource(R.drawable.step_active)
            holder.tvCounter.setTextColor(ResourcesCompat.getColor(context, R.color.white, null))
            holder.tvName.setTextColor(ResourcesCompat.getColor(context, R.color.secondary, null))
            holder.tvNextIndicator.setTextColor(ResourcesCompat.getColor(context, R.color.secondary, null))
        } else {
            holder.tvCounter.setBackgroundResource(R.drawable.step_inactive)
            holder.tvCounter.setTextColor(ResourcesCompat.getColor(context, R.color.primary, null))
            holder.tvName.setTextColor(ResourcesCompat.getColor(context, R.color.primary, null))
            holder.tvNextIndicator.setTextColor(ResourcesCompat.getColor(context, R.color.primary, null))
        }

        if(items[position].number == items.size){
            holder.tvNextIndicator.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = items.size
}