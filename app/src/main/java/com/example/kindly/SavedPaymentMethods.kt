package com.example.kindly

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.backend.PaymentMethodDB
import com.example.kindly.R
import com.example.kindly.backend.PaymentMethodAdapter
import com.example.kindly.fragments.AddPaymentMethod
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SavedPaymentMethods : Fragment(), PaymentMethodAdapter.PaymentMethodClickListener {

    private lateinit var paymentMethodAdapter: PaymentMethodAdapter
    private lateinit var rvPaymentMethods: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_payment_methods, container, false)

        rvPaymentMethods = view.findViewById(R.id.rvPaymentMethods)
        rvPaymentMethods.layoutManager = LinearLayoutManager(context)

        // Fetch user's payment methods and populate the adapter
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            val paymentMethodsCollection = FirebaseFirestore.getInstance().collection("payment_methods")

            paymentMethodsCollection.whereEqualTo("userId", userId)
                .get()
                .addOnSuccessListener { documents ->
                    val paymentMethods = mutableListOf<PaymentMethodDB>()

                    for (document in documents) {
                        val paymentMethod = document.toObject(PaymentMethodDB::class.java)
                        paymentMethods.add(paymentMethod)
                    }

                    // Create and set up the adapter with the payment methods
                    paymentMethodAdapter = PaymentMethodAdapter(paymentMethods)
                    paymentMethodAdapter.setListener(this) // Set the click listener
                    rvPaymentMethods.adapter = paymentMethodAdapter
                }
                .addOnFailureListener { e ->
                    // Handle errors here
                    // You can show a toast or handle the error in your app as needed
                }
        } else {
            // User is not authenticated
            // You can show a toast or handle this condition in your app
        }

        val btnAddPaymentMethod = view.findViewById<FloatingActionButton>(R.id.BtnAddPaymentMethod)

        btnAddPaymentMethod.setOnClickListener {
            // Navigate to the AddPaymentMethod fragment
            val fragment = AddPaymentMethod()
            val transaction = fragmentManager?.beginTransaction()
            transaction?.replace(R.id.frame_container, fragment)
            transaction?.commit()
        }

        return view
    }

    override fun onDeleteClick(paymentMethod: PaymentMethodDB) {
        // Show the delete confirmation dialog
        AlertDialog.Builder(requireContext())
            .setTitle("Delete Payment Method")
            .setMessage("Are you sure you want to delete this payment method?")
            .setPositiveButton("Delete") { _, _ ->
                // Handle the deletion of the payment method here
                // You can call a function to delete the payment method from the database
                // For example, you can implement a function to delete the payment method and show a toast message:
                deletePaymentMethod(paymentMethod)
            }
            .setNegativeButton("Cancel") { _, _ ->
                // Do nothing if the user cancels the deletion
            }
            .show()
    }

    private fun deletePaymentMethod(paymentMethod: PaymentMethodDB) {
        // Implement the logic to delete the payment method from the database
        // For example, you can use Firebase Firestore to delete the document:

        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            val paymentMethodsCollection = FirebaseFirestore.getInstance().collection("payment_methods")

            paymentMethodsCollection
                .whereEqualTo("userId", userId)
                .whereEqualTo("cardNumber", paymentMethod.cardNumber)
                .get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Delete the document
                        document.reference.delete()
                        // Show a toast message for successful deletion
                        Toast.makeText(requireContext(), "Payment method deleted", Toast.LENGTH_SHORT).show()
                        // Move to the ProfileFragment after successful deletion
                        moveToProfileFragment()
                    }
                    // Refresh the payment method list (you may need to re-fetch the data)
                }
                .addOnFailureListener { e ->
                    // Handle errors here
                    // You can show a toast or handle the error in your app as needed
                }
        }
    }

    private fun moveToProfileFragment() {
        // Navigate to the ProfileFragment
        val profileFragment = ProfileFragment()
        val transaction = fragmentManager?.beginTransaction()
        transaction?.replace(R.id.frame_container, profileFragment)
        transaction?.commit()
    }
}