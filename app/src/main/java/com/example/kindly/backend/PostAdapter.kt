package com.example.kindly.backend

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.R
import com.example.kindly.UpdatePost
import com.squareup.picasso.Picasso

class PostAdapter(private var postList: List<Post>, private var postKeys: List<String>) :
    RecyclerView.Adapter<PostAdapter.PostViewHolder>() {

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val charityNameTextView: TextView = itemView.findViewById(R.id.tvCharityNamePost)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescriptionPost)
        val imageView: ImageView = itemView.findViewById(R.id.iv_admin_post_view_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_post, parent, false)
        return PostViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val currentItem = postList[position]
        val currentKey = postKeys[position] // Store the unique key

        holder.charityNameTextView.text = currentItem.name
        holder.descriptionTextView.text = currentItem.description

        if (currentItem.imageUri.isNotBlank()) {
            Picasso.get().load(currentItem.imageUri).into(holder.imageView)
        } else {
            holder.imageView.setImageResource(R.drawable.baseline_image_24)
        }

        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, UpdatePost::class.java)
            intent.putExtra("name", currentItem.name)
            intent.putExtra("description", currentItem.description)
            intent.putExtra("imageUri", currentItem.imageUri)
            intent.putExtra("postId", currentKey)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun updateData(newData: List<Post>, newKeys: List<String>) {
        postList = newData
        postKeys = newKeys // Update the keys using 'var'
        notifyDataSetChanged()
    }
}
