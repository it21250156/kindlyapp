package com.example.kindly
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CharityReqPage : AppCompatActivity() {
    private lateinit var nameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var bankDetailsEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_charity_req_page)

        nameEditText = findViewById(R.id.edtText_req_name)
        emailEditText = findViewById(R.id.edtText_req_email)
        descriptionEditText = findViewById(R.id.edtText_req_description)
        bankDetailsEditText = findViewById(R.id.edtText_req_bankdetails)

        val sendButton: Button = findViewById(R.id.btn_req_send)
        sendButton.setOnClickListener {
            sendFormDetails()
        }
    }

    private fun sendFormDetails() {
        val name = nameEditText.text.toString().trim()
        val email = emailEditText.text.toString().trim()
        val description = descriptionEditText.text.toString().trim()
        val bankDetails = bankDetailsEditText.text.toString().trim()

        if (name.isEmpty() || email.isEmpty() || description.isEmpty() || bankDetails.isEmpty()) {
            // Empty field validation
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        if (!isValidEmail(email)) {
            // Email validation
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show()
            return
        }

        // Send the form details to the admin's email using an email intent
        val adminEmail = "malikadegaldoruwa@gmail.com"
        val subject = "Charity Request Form Submission"
        val message = "Name: $name\nEmail: $email\nDescription: $description\nBank Details: $bankDetails"

        val emailIntent = Intent(Intent.ACTION_SEND)
        emailIntent.type = "message/rfc822"
        emailIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(adminEmail))
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        emailIntent.putExtra(Intent.EXTRA_TEXT, message)

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."))
        } catch (e: Exception) {
            Toast.makeText(this, "Email client not found", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}
