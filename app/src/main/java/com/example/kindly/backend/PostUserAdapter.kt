package com.example.kindly.backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.R

class PostUserAdapter(private val postList: MutableList<Post>) :
    RecyclerView.Adapter<PostUserAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvCharityNamePost)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescriptionPost)
        // Add other views if needed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]

        holder.nameTextView.text = currentItem.name
        holder.descriptionTextView.text = currentItem.description
        // Bind other data as needed
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    // Add an updateData function to update the adapter data
    fun updateData(newData: List<Post>) {
        postList.clear()
        postList.addAll(newData)
        notifyDataSetChanged()
    }
}
