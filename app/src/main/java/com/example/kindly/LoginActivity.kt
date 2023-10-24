package com.example.kindly

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class LoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = Firebase.auth

        val registertxt: TextView = findViewById(R.id.tv_register_now)

        registertxt.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        val loginBtn: Button = findViewById(R.id.btn_login)

        loginBtn.setOnClickListener {
            performLogin()
        }

    }

    private fun performLogin() {
        // get input fields
        val email: EditText = findViewById(R.id.edtText_login_email)
        val password: EditText = findViewById(R.id.edtText_login_password)

        // empty field validation
        if(email.text.isEmpty() || password.text.isEmpty()){
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
            return
        }

        val emailInput = email.text.toString()
        val passwordInput = password.text.toString()

        if(emailInput=="admin@gmail.com" || passwordInput=="admin1234"){
            val intent = Intent(this,AdminHomeActivity::class.java)
            startActivity(intent)
        } else{
            auth.signInWithEmailAndPassword(emailInput, passwordInput)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful){
                        // navigate user home
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)

                        Toast.makeText(
                            baseContext,
                            "Success",
                            Toast.LENGTH_SHORT,
                        ).show()
                    } else{
                        //if sign in fails, display a toast
                        Toast.makeText(
                            baseContext,
                            "Authentication Failed",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }
                .addOnFailureListener{
                    Toast.makeText(
                        baseContext,
                        "Authentication failed. ${it.localizedMessage}",
                        Toast.LENGTH_SHORT,
                    ).show()
                }
        }


    }
}