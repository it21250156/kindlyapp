package com.example.kindly

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val logintxt: TextView = findViewById(R.id.tv_login_now)

        logintxt.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val registerBtn: Button = findViewById(R.id.btn_signup_register)

        registerBtn.setOnClickListener {
            performSignUp()
        }
    }

    private fun performSignUp() {
        val name = findViewById<EditText>(R.id.edtText_signup_name)
        val email = findViewById<EditText>(R.id.edtText_signup_email)
        val mobileNo = findViewById<EditText>(R.id.edtText_signup_mobile)
        val password = findViewById<EditText>(R.id.edtText_signup_password)
        val confirmPassword = findViewById<EditText>(R.id.edtText_signup_confirm_password)

        if (name.text.isEmpty() || email.text.isEmpty() || mobileNo.text.isEmpty() || password.text.isEmpty() || confirmPassword.text.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT)
                .show()
            return
        }

        val inputName = name.text.toString()
        val inputEmail = email.text.toString()
        val inputMobileNum = mobileNo.text.toString()
        val inputPassword = password.text.toString()
        val inputConfirmPassword = confirmPassword.text.toString()

        if (inputPassword == inputConfirmPassword) {
            auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {

                        //abc

                        // if com.example.kindly.User sign up successful, update their profile using their name
                        val user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(inputName)
                            .build()
                        user?.updateProfile(profileUpdates)

                        // Add the user's data to firebase Firestore
                        val userMap = hashMapOf(
                            "name" to inputName,
                            "email" to inputEmail,
                            "mobile_number" to inputMobileNum
                        )
                        firestore.collection("users")
                            .document(user!!.uid)
                            .set(userMap)
                            .addOnSuccessListener {
                                // com.example.kindly.User data added to Firestore successfully
                                Log.d(ContentValues.TAG, "com.example.kindly.User data added to Firestore successfully")

                                // Sign in success, move to home
                                startActivity(Intent(this, MainActivity::class.java))
                                finish()

                                Toast.makeText(
                                    baseContext,
                                    "Success.",
                                    Toast.LENGTH_SHORT,
                                ).show()

                            }
                            .addOnFailureListener { e ->
                                // Error adding user data to Firestore
                                Log.w(ContentValues.TAG, "Error adding user data to Firestore", e)
                            }

                    } else {
                        // If sign in fails, display a message to the user.

                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()

                    }
                }.addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Error occurred ${it.localizedMessage}",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }

        } else {
            Toast.makeText(this, "Please make sure your passwords match.", Toast.LENGTH_SHORT)
                .show()
        }
    }
}