package com.example.kindly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import com.example.kindly.backend.Chat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var chatReference: DatabaseReference

    private lateinit var editTextMessage: EditText
    private lateinit var buttonSend: Button
    private val chatMessages = mutableListOf<Chat>()
    private var currentUser: FirebaseUser? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        chatReference = database.reference.child("chat_messages")

        // Initialize your variables
        editTextMessage = view.findViewById(R.id.editTextMessageContent)
        buttonSend = view.findViewById(R.id.buttonSend)

        // Get the current signed-in user
        currentUser = FirebaseAuth.getInstance().currentUser

        buttonSend.setOnClickListener {
            sendMessage()
        }

        return view
    }

    private fun sendMessage() {
        val messageText = editTextMessage.text.toString()

        // Check if the message is not empty
        if (messageText.isNotEmpty()) {
            // Ensure there is a current user
            currentUser?.displayName?.let { senderName ->

                // Get the current timestamp in milliseconds
                val timestamp = System.currentTimeMillis()

                // Create a ChatMessage object with the sender's name and message content
                val chatMessage = Chat(senderName, messageText, timestamp)

                // Push the chatMessage data to the Firebase Realtime Database
                val newChatRef = chatReference.push()
                newChatRef.setValue(chatMessage)

                // Notify your RecyclerView adapter that the data has changed
                // Add your code to update the RecyclerView adapter here

                // Clear the message input field
                editTextMessage.text.clear()
            }
        }

    }

}