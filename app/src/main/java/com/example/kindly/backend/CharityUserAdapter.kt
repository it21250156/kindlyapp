
package com.example.kindly.backend

import android.graphics.drawable.Drawable
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

class CharityUserAdapter(private val charityList: MutableList<CharityDB>) :
    RecyclerView.Adapter<CharityUserAdapter.CharityViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(charity: CharityDB)
    }

    private var listener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class CharityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tvCharityName)
        val descriptionTextView: TextView = itemView.findViewById(R.id.tvDescription)
        val ivImage: ImageView = itemView.findViewById(R.id.ivImage)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener?.onItemClick(charityList[position])
                }
            }
        }
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

        // Load the image using Glide
        Glide.with(holder.itemView)
            .load(currentItem.imageUri)
            .placeholder(R.drawable.baseline_image_24)
            .error(R.drawable.applogo)
            .addListener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    // Handle image loading failure here
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: Target<Drawable>?,
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
        return charityList.size
    }

    fun updateData(newData: List<CharityDB>) {
        charityList.clear()
        charityList.addAll(newData)
        notifyDataSetChanged()
    }
}
