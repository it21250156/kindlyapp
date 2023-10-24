package com.example.kindly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class ResetPasswordFragment : Fragment() {

    private lateinit var rAuth: FirebaseAuth
    private lateinit var rDatabase: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_reset_password, container, false)

        rAuth = FirebaseAuth.getInstance()
        rDatabase = FirebaseFirestore.getInstance()

        val btnSendEmail = view.findViewById<Button>(R.id.btn_sendEmail)
        val btnCancel = view.findViewById<Button>(R.id.btn_resetPasswordCancel)

        btnSendEmail.setOnClickListener {
            performSendEmail()
        }

        btnCancel.setOnClickListener {
            val fragment = ProfileFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container,fragment)?.commit()
        }


        return view
    }

    private fun performSendEmail() {
        val uRef = rAuth.currentUser?.let { rDatabase.collection("users").document(it.uid) }
        uRef!!.get().addOnSuccessListener { documentSnapshot ->
            val email = documentSnapshot.getString("email")
            if(email != null){
                rAuth.sendPasswordResetEmail(email).addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        Toast.makeText(activity, "Reset Password email sent.", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(activity, "Failed to send Reset Password email.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else{
                Toast.makeText(activity, "Failed to retrieve user email address.", Toast.LENGTH_SHORT).show()
            }
        }
            .addOnFailureListener{ e ->
                Toast.makeText(activity, "Failed to retrieve an email address: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


}