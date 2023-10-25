package com.example.kindly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.google.firebase.firestore.QueryDocumentSnapshot

class SavedPaymentMethods : Fragment() {

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

        return view
    }
}
