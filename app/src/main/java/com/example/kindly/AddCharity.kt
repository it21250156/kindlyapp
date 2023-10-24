package com.example.kindly

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kindly.databinding.ActivityAddCharityBinding // Import the View Binding class

class AddCharity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var binding: ActivityAddCharityBinding // Initialize View Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCharityBinding.inflate(layoutInflater) // Initialize View Binding

        setContentView(binding.root) // Use binding.root to set the content view

        binding.ivAdminCharImg.setOnClickListener {
            openFileChooser()
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
            val imageUri: Uri? = data.data
            // Update the ImageView with the selected image
            binding.ivAdminCharImg.setImageURI(imageUri)
        }
    }
}

