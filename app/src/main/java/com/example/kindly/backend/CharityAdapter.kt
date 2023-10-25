package com.example.kindly.backend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.R
import com.example.kindly.UpdateCharity

import com.squareup.picasso.Picasso

class CharityAdapter(private var charityList: List<CharityDB>, private var charityKeys: List<String>) :
    RecyclerView.Adapter<CharityAdapter.CharityViewHolder>() {

    inner class CharityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvCharityName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        val imageView: ImageView = itemView.findViewById(R.id.ivImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharityViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_charity, parent, false)
        return CharityViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CharityViewHolder, position: Int) {
        val currentItem = charityList[position]
        val currentKey = charityKeys[position] // Store the unique key

        holder.nameTextView.text = currentItem.name
        holder.descriptionTextView.text = currentItem.description

        if (currentItem.imageUri.isNotBlank()) {
            Picasso.get().load(currentItem.imageUri).into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.baseline_image_24) // Replace with your placeholder image
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UpdateCharity::class.java)
            intent.putExtra("name", currentItem.name)
            intent.putExtra("address", currentItem.address)
            intent.putExtra("contact", currentItem.contact)
            intent.putExtra("email", currentItem.email)
            intent.putExtra("description", currentItem.description)
            intent.putExtra("imageUri", currentItem.imageUri)
            intent.putExtra("charityId", currentKey)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return charityList.size
    }

    fun updateData(newData: List<CharityDB>, newKeys: List<String>) {
        charityList = newData
        charityKeys = newKeys // Update the keys using 'var'
        notifyDataSetChanged()
    }
}


