package com.example.kindly.backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.example.kindly.R

class PostUserAdapter(private val postList: MutableList<Post>) :
    RecyclerView.Adapter<PostUserAdapter.PostViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(post: Post)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class PostViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvCharityNamePost)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescriptionPost)
        val ivImage: ImageView = itemView.findViewById(R.id.iv_admin_post_view_img)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(postList[position])
                }
            }
        }
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

        // Load the image using Glide
        Glide.with(holder.itemView)
            .load(currentItem.imageUri)
            .placeholder(R.drawable.baseline_image_24)
            .error(R.drawable.applogo)
            .listener(object : RequestListener<android.graphics.drawable.Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<android.graphics.drawable.Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Handle image loading failure here
                    return false
                }

                override fun onResourceReady(
                    resource: android.graphics.drawable.Drawable?,
                    model: Any?,
                    target: Target<android.graphics.drawable.Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Image loaded successfully
                    return false
                }
            })
            .into(holder.ivImage)
    }

    override fun getItemCount(): Int {
        return postList.size
    }

    fun updateData(newData: List<Post>) {
        postList.clear()
        postList.addAll(newData)
        notifyDataSetChanged()
    }
}
