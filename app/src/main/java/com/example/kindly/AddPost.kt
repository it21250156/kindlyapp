package com.example.kindly

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kindly.backend.Post
import com.example.kindly.databinding.ActivityAddPostBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase

class AddPost : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var binding: ActivityAddPostBinding
    private lateinit var database: FirebaseDatabase
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivAdminPostImg.setOnClickListener {
            openFileChooser()
        }

        database = FirebaseDatabase.getInstance()

        binding.btnAddPost.setOnClickListener {
            // Retrieve input values
            val name = binding.edtTextPostName.text.toString()
            val description = binding.edtTextPostDescription.text.toString()

            // Check if any of the fields are empty
            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri != null) {
                // You can create a reference to a specific "posts" node in Firebase
                val postsRef = database.reference.child("posts")

                // Create a new child node for the post with a unique ID
                val postRef = postsRef.push()

                // Set the data you want to save
                val imageUrl = imageUri.toString()
                val postData = Post(name, description, imageUrl)

                // Save the data to Firebase
                postRef.setValue(postData).addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Data saved successfully
                        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this, PostManageAdmin::class.java)
                        startActivity(intent)

                    } else {
                        // Error occurred while saving data
                        val error = task.exception?.message
                        Toast.makeText(this, "Error occurred: $error", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                // Handle the case where no image was selected
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openFileChooser() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            binding.ivAdminPostImg.setImageURI(imageUri)
        }
    }
}
