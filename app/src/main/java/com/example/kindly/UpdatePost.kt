package com.example.kindly

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.example.kindly.backend.Post
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UpdatePost : AppCompatActivity() {
    private lateinit var postId: String
    private val database = FirebaseDatabase.getInstance()
    private val postsRef = database.getReference("posts")
    private var imageUri: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_post)

        // Retrieve data passed from the PostAdapter (or wherever you're coming from)
        val postName = intent.getStringExtra("name")
        val postDescription = intent.getStringExtra("description")
        imageUri = intent.getStringExtra("imageUri") // Retrieve imageUri here
        postId = intent.getStringExtra("postId").toString()

        val nameEditText = findViewById<EditText>(R.id.edtText_post_name_update)
        val descriptionEditText = findViewById<EditText>(R.id.edtText_post_description_update)
        val imageView = findViewById<ImageView>(R.id.ivAdminPostUpdateimg)

        nameEditText.setText(postName)
        descriptionEditText.setText(postDescription)

        if (!imageUri.isNullOrBlank()) {
            Picasso.get().load(imageUri).into(imageView)
        }

        val updateButton = findViewById<Button>(R.id.btnUpdatePost)
        val deleteButton = findViewById<Button>(R.id.btnDeletePost)

        updateButton.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedDescription = descriptionEditText.text.toString()

            if (updatedName.isNotBlank() && updatedDescription.isNotBlank()) {
                updatePostData(postId, updatedName, updatedDescription)
            } else {
                Toast.makeText(this, "Name and description cannot be empty", Toast.LENGTH_SHORT).show()
            }
        }

        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun updatePostData(postId: String, name: String, description: String) {
        val postRef = postsRef.child(postId)

        // Update the specific fields in Firebase Realtime Database
        postRef.child("name").setValue(name)
        postRef.child("description").setValue(description)

        Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this post?")
            .setPositiveButton("Yes") { _, _ ->
                deletePost(postId)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deletePost(postId: String) {
        postsRef.child(postId).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Delete Successful", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show()
        }
    }
}
