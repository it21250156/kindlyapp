package com.example.kindly

import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class UpdatePost : AppCompatActivity() {
    private lateinit var postId: String
    private val database = FirebaseDatabase.getInstance()
    private val postsRef = database.getReference("posts")
    private lateinit var storageReference: StorageReference
    private lateinit var imageView: ImageView // Declare imageView here
    private var imageUri: Uri? = null
    private var isNewImageSelected = false // Track if a new image is selected

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_post)

        // Retrieve data passed from the PostAdapter (or wherever you're coming from)
        val postName = intent.getStringExtra("name")
        val postDescription = intent.getStringExtra("description")
        imageUri = Uri.parse(intent.getStringExtra("imageUri")) // Retrieve imageUri here
        postId = intent.getStringExtra("postId").toString()

        val nameEditText = findViewById<EditText>(R.id.edtText_post_name_update)
        val descriptionEditText = findViewById<EditText>(R.id.edtText_post_description_update)
        imageView = findViewById<ImageView>(R.id.ivAdminPostUpdateimg) // Initialize imageView

        nameEditText.setText(postName)
        descriptionEditText.setText(postDescription)

        if (imageUri != null) {
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
                if (imageUri != null && isNewImageSelected) {
                    uploadImageAndUpdatePost(postId, updatedName, updatedDescription)
                } else {
                    updatePostData(postId, updatedName, updatedDescription, imageUri.toString())
                }
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
            imageUri = data.data
            isNewImageSelected = true // A new image is selected
            if (imageUri != null) {
                // Load and display the selected image in the ImageView
                Picasso.get().load(imageUri).into(imageView)
            }
        }
    }

    private fun uploadImageAndUpdatePost(postId: String, name: String, description: String) {
        storageReference = FirebaseStorage.getInstance().reference
        val imageName = "post_image_${System.currentTimeMillis()}.jpg"
        val imageRef = storageReference.child("post_images/$imageName")

        val uploadTask: UploadTask = imageRef.putFile(imageUri!!)
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    updatePostData(postId, name, description, imageUrl)
                }
            } else {
                val error = task.exception?.message
                Toast.makeText(this, "Error occurred while uploading image: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePostData(postId: String, name: String, description: String, imageUrl: String) {
        val postRef = postsRef.child(postId)

        // Update the specific fields in Firebase Realtime Database
        postRef.child("name").setValue(name)
        postRef.child("description").setValue(description)
        // Use the provided image URI or the existing image URI
        postRef.child("imageUri").setValue(imageUrl)

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
