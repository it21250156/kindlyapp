package com.example.kindly

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kindly.backend.Post
import com.example.kindly.databinding.ActivityAddPostBinding
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask

class AddPost : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var binding: ActivityAddPostBinding
    private lateinit var database: FirebaseDatabase
    private var imageUri: Uri? = null
    private var storageReference: StorageReference

    init {
        storageReference = FirebaseStorage.getInstance().reference
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivAdminPostImg.setOnClickListener {
            openFileChooser()
        }

        database = FirebaseDatabase.getInstance()
        val postsRef = database.reference.child("posts")

        binding.btnAddPost.setOnClickListener {
            val name = binding.edtTextPostName.text.toString()
            val description = binding.edtTextPostDescription.text.toString()

            if (name.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri != null) {
                val imageName = "post_image_${System.currentTimeMillis()}.jpg"
                val imageRef = storageReference.child("post_images/$imageName")

                val uploadTask: UploadTask = imageRef.putFile(imageUri!!)
                uploadTask.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        imageRef.downloadUrl.addOnSuccessListener { uri ->
                            val imageUrl = uri.toString()
                            val postData = Post(name, description, imageUrl)

                            val postRef = postsRef.push()
                            postRef.setValue(postData).addOnCompleteListener { dbTask ->
                                if (dbTask.isSuccessful) {
                                    Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                                    val intent = Intent(this, PostManageAdmin::class.java)
                                    startActivity(intent)
                                } else {
                                    val error = dbTask.exception?.message
                                    Toast.makeText(this, "Error occurred: $error", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    } else {
                        val error = task.exception?.message
                        Toast.makeText(this, "Error occurred while uploading image: $error", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
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
