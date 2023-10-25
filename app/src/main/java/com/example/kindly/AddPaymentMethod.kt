package com.example.kindly.fragments

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
import com.example.kindly.R
import com.example.kindly.backend.PaymentMethodDB

class AddPaymentMethod : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_payment_method, container, false)

        val EtCredictCardNo = view.findViewById<EditText>(R.id.EtCredictCardNo)
        val EtCvv = view.findViewById<EditText>(R.id.EtCvv)
        val EtExpDate = view.findViewById<EditText>(R.id.EtExpDate)
        val btnAddCard = view.findViewById<Button>(R.id.BtnAddcard)

        btnAddCard.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                val cardNumber = EtCredictCardNo.text.toString()
                val cvv = EtCvv.text.toString()
                val expDate = EtExpDate.text.toString()

                val paymentMethod = PaymentMethodDB(userId, cardNumber, cvv, expDate)

                val paymentMethodsCollection = FirebaseFirestore.getInstance().collection("payment_methods")

                paymentMethodsCollection.add(paymentMethod)
                    .addOnSuccessListener { documentReference ->
                        // Payment method added successfully
                        // You can also handle this event if needed
                        Toast.makeText(context, "Payment method added successfully.", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener { e ->
                        // Handle errors here
                        Toast.makeText(context, "Error: Failed to add payment method.", Toast.LENGTH_SHORT).show()
                    }
            } else {
                // User is not authenticated
                Toast.makeText(context, "User is not authenticated. Please log in.", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }
}
