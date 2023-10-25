package com.example.kindly.backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.R

class ChatAdapter(private val chatMessages: List<Chat>) : RecyclerView.Adapter<ChatAdapter.ChatViewHolder>() {
    inner class ChatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Define views in the chat item XML
        val senderName: TextView = itemView.findViewById(R.id.textViewChatSender)
        val messageContent: TextView = itemView.findViewById(R.id.textViewChatMessage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChatViewHolder {
        // Inflate the chat item layout and create a ViewHolder
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.chat_item, parent, false)
        return ChatViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChatViewHolder, position: Int) {
        // Bind data to views in the ViewHolder starting from the last item in the list
        val chatMessage = chatMessages[chatMessages.size - position - 1]
        holder.senderName.text = chatMessage.sender
        holder.messageContent.text = chatMessage.message
    }

    override fun getItemCount() = chatMessages.size
}
