package com.example.habittracker.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.habittracker.R
import com.example.habittracker.shared.utils.ProfileImageListConstants

class ProfileImageAdapter(private val images: List<String>) :
    RecyclerView.Adapter<ProfileImageAdapter.PictureViewHolder>() {
    var selectedImage: Int = RecyclerView.NO_POSITION

    class PictureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.iv_profile_image)
        val container: View = itemView
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.rv_profile_image, parent, false)
        return PictureViewHolder(view)
    }

    override fun onBindViewHolder(
        holder: PictureViewHolder,
        @SuppressLint("RecyclerView") position: Int
    ) {
        val image = images[position]
        val imageResId = ProfileImageListConstants.images[image]
        holder.imageView.setImageResource(imageResId ?: R.drawable.img_photo_profile_placeholder)

        // Set active color
        val selectedColor = ContextCompat.getColor(holder.itemView.context, R.color.secondary)
        if (position == selectedImage) {
            holder.container.setBackgroundColor(selectedColor)
        } else {
            holder.container.setBackgroundColor(Color.TRANSPARENT)
        }
        holder.imageView.setOnClickListener {
            val previousPosition = selectedImage
            selectedImage = position
            notifyItemChanged(position)
            notifyItemChanged(previousPosition)
        }
    }

    override fun getItemCount(): Int = images.size
}
