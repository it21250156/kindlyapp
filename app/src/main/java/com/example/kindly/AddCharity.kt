package com.example.kindly

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kindly.backend.CharityDB
import com.example.kindly.databinding.ActivityAddCharityBinding
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.database.FirebaseDatabase

class AddCharity : AppCompatActivity() {
    private val PICK_IMAGE_REQUEST = 1
    private lateinit var binding: ActivityAddCharityBinding
    private lateinit var database: FirebaseDatabase
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddCharityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivAdminCharImg.setOnClickListener {
            openFileChooser()
        }

        database = FirebaseDatabase.getInstance()
        val charitiesRef = database.reference.child("charities")

        binding.BtnAddCharity.setOnClickListener {
            val name = binding.edtTextAdminCharName.text.toString()
            val address = binding.edtTextAdminCharAddress.text.toString()
            val contact = binding.edtTextAdminCharContact.text.toString()
            val email = binding.edtTextAdminCharEmail.text.toString()
            val description = binding.edtTextAdminCharDescription.text.toString()

            if (name.isEmpty() || address.isEmpty() || contact.isEmpty() || email.isEmpty() || description.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (imageUri != null) {
                val charityRef = charitiesRef.push()
                val imageUrl = imageUri.toString()
                val charityData = CharityDB(name, address, contact, email, description, imageUrl)

                charityRef.setValue(charityData).addOnCompleteListener(OnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Data saved successfully", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, CharityManageAdmin::class.java)
                        startActivity(intent)
                    } else {
                        val error = task.exception?.message
                        Toast.makeText(this, "Error occurred: $error", Toast.LENGTH_SHORT).show()
                    }
                })
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
            binding.ivAdminCharImg.setImageURI(imageUri)
        }
    }

    fun cancel(view: View) {
        val intent = Intent(this, CharityManageAdmin::class.java)
        startActivity(intent)
    }
}
