package com.example.kindly.backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.R

class CharityUserAdapter(private val charityList: MutableList<CharityDB>) :
    RecyclerView.Adapter<CharityUserAdapter.CharityViewHolder>() {

    inner class CharityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvCharityName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        // Add other views if needed
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
        // Bind other data as needed
    }

    override fun getItemCount(): Int {
        return charityList.size
    }

    // Add an updateData function to update the adapter data
    fun updateData(newData: List<CharityDB>) {
        charityList.clear()
        charityList.addAll(newData)
        notifyDataSetChanged()
    }
}