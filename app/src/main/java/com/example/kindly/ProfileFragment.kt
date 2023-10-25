package com.example.kindly

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
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

        val btnEditProfile = view.findViewById<Button>(R.id.btn_edit_profile)
        val btnResetPassword = view.findViewById<Button>(R.id.btn_reset_password)
        val btnLogout = view.findViewById<Button>(R.id.btn_logout)
        val btnViewPayment = view.findViewById<Button>(R.id.btnViewPayment)
        val btnProfileDonationHistory = view.findViewById<Button>(R.id.btn_profile_donation_history)

        btnEditProfile.setOnClickListener {
            val fragment = EditProfileFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, fragment)
            transaction?.commit()
        }

        btnResetPassword.setOnClickListener {
            val fragment = ResetPasswordFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, fragment)
            transaction?.commit()
        }

        btnViewPayment.setOnClickListener {
            val fragment = SavedPaymentMethods()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, fragment)
            transaction?.commit()
        }

        btnLogout.setOnClickListener {
            // Logout from Firebase
            auth.signOut()

            // Return to the login screen
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)

            // To prevent the user from going back
            activity?.finish()
        }

        btnProfileDonationHistory.setOnClickListener {
            val fragment = DonationHistory()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, fragment)
            transaction?.commit()
        }

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

                    // Make the EditText fields read-only
                    edtName.isFocusable = false
                    edtName.isFocusableInTouchMode = false
                    edtEmail.isFocusable = false
                    edtEmail.isFocusableInTouchMode = false
                    edtMobileNo.isFocusable = false
                    edtMobileNo.isFocusableInTouchMode = false
                    welcomeName.isFocusable = false
                    welcomeName.isFocusableInTouchMode = false
                } else {
                    // The document does not exist
                    Toast.makeText(context, "User not found.", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { exception ->
                // Handle the error
                Toast.makeText(context, "Error: Failed to retrieve data.", Toast.LENGTH_LONG).show()
            }
        } else {
            // User is not authenticated
            Toast.makeText(context, "User is not authenticated. Please log in.", Toast.LENGTH_LONG).show()
        }

        return view
    }
}
