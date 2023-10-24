package com.example.kindly.backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.R

import com.squareup.picasso.Picasso

class CharityAdapter(private var charityList: List<CharityDB>) :
    RecyclerView.Adapter<CharityAdapter.CharityViewHolder>() {

    inner class CharityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvCharityName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        val imageView: ImageView = itemView.findViewById(R.id.ivImage) // Add an ImageView for the image
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharityViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_charity, parent, false)
        return CharityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CharityViewHolder, position: Int) {
        val currentItem = charityList[position]

        holder.nameTextView.text = currentItem.name
        holder.descriptionTextView.text = currentItem.description

        // Load the image using Picasso
        if (currentItem.imageUri.isNotBlank()) {
            Picasso.get().load(currentItem.imageUri).into(holder.imageView)
        } else {
            // Set a default image or placeholder if no image is provided
            holder.imageView.setImageResource(R.drawable.baseline_image_24) // Replace with your placeholder image
        }

        // Bind other data as needed
    }

    override fun getItemCount(): Int {
        return charityList.size
    }

    fun updateData(newData: List<CharityDB>) {
        charityList = newData
        notifyDataSetChanged()
    }
}

