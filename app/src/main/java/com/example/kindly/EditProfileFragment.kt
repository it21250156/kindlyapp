package com.example.kindly

import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


class EditProfileFragment : Fragment() {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()


    private var currentUser = FirebaseAuth.getInstance().currentUser
    private var userDocRef = FirebaseFirestore.getInstance().collection("users").document(currentUser!!.uid)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)

        val btnCancel = view.findViewById<Button>(R.id.btnUpdateProfileBack)

        btnCancel.setOnClickListener {
            val fragment = ProfileFragment()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container,fragment)?.commit()
        }

        // Find your EditText fields by ID
        val edtName = view.findViewById<EditText>(R.id.edtText_update_profile_name)
        val edtEmail = view.findViewById<EditText>(R.id.edtText_update_profile_email)
        val edtMobileNo = view.findViewById<EditText>(R.id.edtText_update_profile_mobile)

        // Retrieve the user's data and populate the EditText fields
        val user = auth.currentUser
        if (user != null) {
            val uid = user.uid
            val userDocRef = db.collection("users").document(uid)

            userDocRef.get().addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val data = documentSnapshot.data

                    val profileName = data?.get("name") as String
                    val profileEmail = data?.get("email") as String
                    val profileMobileNo = data?.get("mobile_number") as String

                    edtName.setText(profileName)
                    edtEmail.setText(profileEmail)
                    edtMobileNo.setText(profileMobileNo)
                }
            }
        }



        // Allow the user to edit the EditText fields
        edtName.isEnabled = true
        edtEmail.isEnabled = true
        edtMobileNo.isEnabled = true

        // Find the "Save Changes" button by ID
        val btnSaveChanges = view.findViewById<Button>(R.id.btnUpdateProfileSave)

        // Add a click listener to the "Save Changes" button
        btnSaveChanges.setOnClickListener {
            // Get the edited values from the EditText fields
            val newName = edtName.text.toString()
            val newEmail = edtEmail.text.toString()
            val newMobileNo = edtMobileNo.text.toString()

            // Update the data in the Firestore database
            val userUpdate = hashMapOf(
                "name" to newName,
                "email" to newEmail,
                "mobile_number" to newMobileNo
            )

            if (user != null) {
                val uid = user.uid
                val userDocRef = db.collection("users").document(uid)

                userDocRef.update(userUpdate as Map<String, Any>).addOnSuccessListener {
                    // Data updated successfully
                    val context = requireContext() // or use 'activity' if required
                    val message = "Updated Successfully"
                    val duration = Toast.LENGTH_SHORT // or Toast.LENGTH_LONG

                    val toast = Toast.makeText(context, message, duration)
                    toast.show()


                    // Navigate back to the profile screen

                    val fragment = ProfileFragment()
                    val transaction = fragmentManager?.beginTransaction()
                    transaction?.replace(R.id.frame_container, fragment)?.commit()



                }.addOnFailureListener{ e ->
                    // Handle the error if the update fails
                    val context = requireContext() // or use 'activity' if required
                    val message = "Update Failed"
                    val duration = Toast.LENGTH_SHORT // or Toast.LENGTH_LONG

                    val toast = Toast.makeText(context, message, duration)
                    toast.show()

                }
            }
        }

        val btnDeleteUser = view.findViewById<Button>(R.id.btnDeleteProfile)

        btnDeleteUser.setOnClickListener {
            performDeleteUser()
        }

        return view
    }

    private fun performDeleteUser() {
        // show a confirmation dialog to user
        AlertDialog.Builder(requireContext())
            .setTitle("Delete User Account")
            .setMessage("Are you sure you want to delete your account?")
            .setPositiveButton("Yes, I'm Sure"){_, _ ->
                // delete user from database and sign out the user
                userDocRef.delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Account Deleted Successfully", Toast.LENGTH_SHORT).show()
                        auth.signOut()

                        val intent = Intent(requireContext(), LoginActivity::class.java)
                        startActivity(intent)
                        requireActivity().finish()
                    }
                    .addOnFailureListener{ e ->
                        Log.e(ContentValues.TAG, "Error deleting user document", e)
                        Toast.makeText(context, "Failed to delete account", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }


}