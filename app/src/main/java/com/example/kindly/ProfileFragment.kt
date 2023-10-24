package com.example.kindly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Get the currently authenticated user
        val user = auth.currentUser

        // Check if the user is authenticated
        if (user != null) {
            // User is authenticated, retrieve data from Firestore
            val uid = user.uid // Assuming you are using Firebase Authentication

            // Reference to the Firestore document for the user
            val userDocRef = db.collection("users").document(uid)

            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    // The document exists, retrieve data
                    val data = documentSnapshot.data

                    // Access and set data in your EditText fields
                    val profileName = data?.get("name") as String // Replace "name" with the actual field name
                    val profileEmail = data?.get("email") as String // Replace "email" with the actual field name
                    val profileMobileNo: String = data?.get("mobile_number") as String

                    // Find your EditText fields by ID
                    val edtName = view.findViewById<EditText>(R.id.edtText_profile_name)
                    val edtEmail = view.findViewById<EditText>(R.id.edtText_profile_email)
                    val edtMobileNo = view.findViewById<EditText>(R.id.edtText_profile_mobile)
                    val welcomeName = view.findViewById<EditText>(R.id.editText_welcome_name)

                    // Set data to EditText fields
                    edtName.setText(profileName)
                    edtEmail.setText(profileEmail)
                    edtMobileNo.setText(profileMobileNo)
                    welcomeName.setText(profileName)
                } else {
                    // The document does not exist, handle accordingly
                }
            }.addOnFailureListener { exception ->
                // Handle the error
            }
        } else {
            // User is not authenticated, handle accordingly
        }


        return view
    }

}