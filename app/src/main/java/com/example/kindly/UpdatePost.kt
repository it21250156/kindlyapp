package com.example.kindly

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class UpdatePost : AppCompatActivity() {
    private lateinit var postId: String
    private val database = FirebaseDatabase.getInstance()
    private val postsRef = database.getReference("posts")
    private var imageUri: String? = null
    private lateinit var imageView: ImageView // Declare imageView here

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

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
        imageView = findViewById<ImageView>(R.id.ivAdminPostUpdateimg) // Initialize imageView

        nameEditText.setText(postName)
        descriptionEditText.setText(postDescription)

        if (!imageUri.isNullOrBlank()) {
            Picasso.get().load(imageUri).into(imageView)
        }

        val updateButton = findViewById<Button>(R.id.btnUpdatePost)
        val deleteButton = findViewById<Button>(R.id.btnDeletePost)
        val pickImageButton = findViewById<Button>(R.id.btnPickImage)

        pickImageButton.setOnClickListener {
            pickImage()
        }

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

    private fun pickImage() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        try {
            startActivityForResult(intent, PICK_IMAGE_REQUEST)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, "No gallery app found", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            val selectedImageUri = data.data
            if (selectedImageUri != null) {
                // Load and display the selected image in the ImageView
                Picasso.get().load(selectedImageUri).into(imageView)
                // Save the selected image URI for later use when updating the post
                imageUri = selectedImageUri.toString()
            }
        }
    }

    private fun updatePostData(postId: String, name: String, description: String) {
        val postRef = postsRef.child(postId)

        // Update the specific fields in Firebase Realtime Database
        postRef.child("name").setValue(name)
        postRef.child("description").setValue(description)
        // Update the image URI
        postRef.child("imageUri").setValue(imageUri)

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
