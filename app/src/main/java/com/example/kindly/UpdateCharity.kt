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
import com.example.kindly.backend.CharityDB
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.squareup.picasso.Picasso

class UpdateCharity : AppCompatActivity() {
    private lateinit var charityId: String
    private val database = FirebaseDatabase.getInstance()
    private val charitiesRef = database.getReference("charities")
    private lateinit var storageReference: StorageReference
    private lateinit var imageView: ImageView
    private var imageUri: Uri? = null
    private var isNewImageSelected = false // Track if a new image is selected

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_charity)

        // Retrieve data passed from the CharityAdapter
        val name = intent.getStringExtra("name")
        val address = intent.getStringExtra("address")
        val contact = intent.getStringExtra("contact")
        val email = intent.getStringExtra("email")
        val description = intent.getStringExtra("description")
        imageUri = Uri.parse(intent.getStringExtra("imageUri")) // Retrieve imageUri here
        charityId = intent.getStringExtra("charityId").toString()

        val nameEditText = findViewById<EditText>(R.id.edtText_admin_CharName)
        val addressEditText = findViewById<EditText>(R.id.edtText_admin_char_address)
        val contactEditText = findViewById<EditText>(R.id.edtText_admin_char_contact)
        val emailEditText = findViewById<EditText>(R.id.edtText_admin_char_email)
        val descriptionEditText = findViewById<EditText>(R.id.edtText_admin_char_description)
        imageView = findViewById<ImageView>(R.id.iv_admin_char_img)

        nameEditText.setText(name)
        addressEditText.setText(address)
        contactEditText.setText(contact)
        emailEditText.setText(email)
        descriptionEditText.setText(description)

        if (imageUri != null) {
            Picasso.get().load(imageUri).into(imageView)
        }

        val updateButton = findViewById<Button>(R.id.BtnUpdateCharity)
        val deleteButton = findViewById<Button>(R.id.btnDelete)
        val pickImageButton = findViewById<Button>(R.id.btnPickImage)

        pickImageButton.setOnClickListener {
            pickImage()
        }

        updateButton.setOnClickListener {
            val updatedName = nameEditText.text.toString()
            val updatedAddress = addressEditText.text.toString()
            val updatedContact = contactEditText.text.toString()
            val updatedEmail = emailEditText.text.toString()
            val updatedDescription = descriptionEditText.text.toString()

            // Check if a new image is selected
            if (imageUri != null && isNewImageSelected) {
                uploadImageAndUpdateCharity(charityId, updatedName, updatedAddress, updatedContact, updatedEmail, updatedDescription)
            } else {
                updateCharityData(charityId, updatedName, updatedAddress, updatedContact, updatedEmail, updatedDescription, imageUri.toString())
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
                Picasso.get().load(imageUri).into(imageView)
            }
        }
    }

    private fun uploadImageAndUpdateCharity(charityId: String, name: String, address: String, contact: String, email: String, description: String) {
        storageReference = FirebaseStorage.getInstance().reference
        val imageName = "charity_image_${System.currentTimeMillis()}.jpg"
        val imageRef = storageReference.child("charity_images/$imageName")

        val uploadTask: UploadTask = imageRef.putFile(imageUri!!)
        uploadTask.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    val imageUrl = uri.toString()
                    updateCharityData(charityId, name, address, contact, email, description, imageUrl)
                }
            } else {
                val error = task.exception?.message
                Toast.makeText(this, "Error occurred while uploading image: $error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateCharityData(charityId: String, name: String, address: String, contact: String, email: String, description: String, imageUrl: String) {
        val charityRef = charitiesRef.child(charityId)

        // Update the specific fields in Firebase Realtime Database
        charityRef.child("name").setValue(name)
        charityRef.child("address").setValue(address)
        charityRef.child("contact").setValue(contact)
        charityRef.child("email").setValue(email)
        charityRef.child("description").setValue(description)
        // Use the provided image URI or the existing image URI
        charityRef.child("imageUri").setValue(imageUrl)

        Toast.makeText(this, "Update Successful", Toast.LENGTH_SHORT).show()
        finish()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Confirm Delete")
            .setMessage("Are you sure you want to delete this charity?")
            .setPositiveButton("Yes") { _, _ ->
                deleteCharity(charityId)
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }

    private fun deleteCharity(charityId: String) {
        charitiesRef.child(charityId).removeValue().addOnSuccessListener {
            Toast.makeText(this, "Delete Successful", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "Delete Failed", Toast.LENGTH_SHORT).show()
        }
    }
}



