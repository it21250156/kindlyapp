package com.example.kindly.fragments

import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.kindly.R
import com.example.kindly.SavedPaymentMethods
import com.example.kindly.backend.PaymentMethodDB
import java.util.regex.Pattern

class AddPaymentMethod : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_add_payment_method, container, false)

        val EtCreditCardNo = view.findViewById<EditText>(R.id.EtCredictCardNo)
        val EtCvv = view.findViewById<EditText>(R.id.EtCvv)
        val EtExpDate = view.findViewById<EditText>(R.id.EtExpDate)
        val btnAddCard = view.findViewById<Button>(R.id.BtnAddcard)

        // Set input field limits
        EtCreditCardNo.filters = arrayOf(InputFilter.LengthFilter(16)) // Limit card number to 16 digits
        EtCvv.filters = arrayOf(InputFilter.LengthFilter(3)) // Limit CVV to 3 digits
        EtExpDate.filters = arrayOf(InputFilter.LengthFilter(5)) // Limit expDate to 4 digits (MM/YYYY)

        // Set input type for card number and CVV fields
        EtCreditCardNo.inputType = InputType.TYPE_CLASS_NUMBER
        EtCvv.inputType = InputType.TYPE_CLASS_NUMBER

        btnAddCard.setOnClickListener {
            val userId = FirebaseAuth.getInstance().currentUser?.uid

            if (userId != null) {
                val cardNumber = EtCreditCardNo.text.toString()
                val cvv = EtCvv.text.toString()
                val expDate = EtExpDate.text.toString()

                if (!isValidCardNumber(cardNumber)) {
                    Toast.makeText(context, "Invalid card number. It should be 16 digits.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!isValidCvv(cvv)) {
                    Toast.makeText(context, "Invalid CVV. It should be 3 digits.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                if (!isValidExpDate(expDate)) {
                    Toast.makeText(context, "Invalid expiration date. Please use digits for MM/YYYY.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Separate the month and year with a "/"
                val month = expDate.substring(0, 2)
                val year = expDate.substring(2)

                val paymentMethod = PaymentMethodDB(userId, cardNumber, cvv, "$month/$year")

                val paymentMethodsCollection = FirebaseFirestore.getInstance().collection("payment_methods")

                paymentMethodsCollection.add(paymentMethod)
                    .addOnSuccessListener { documentReference ->
                        // Payment method added successfully
                        Toast.makeText(context, "Payment method added successfully.", Toast.LENGTH_SHORT).show()

                        // Move to the SavedPaymentMethods fragment after successful insertion
                        val fragment = SavedPaymentMethods()
                        val fragmentManager = parentFragmentManager
                        val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                        transaction.replace(R.id.frame_container, fragment)
                        transaction.addToBackStack(null)
                        transaction.commit()
                    }
                    .addOnFailureListener { e ->
                        // Handle errors here
                        Toast.makeText(context, "Error: Failed to add payment method.", Toast.LENGTH_LONG).show()
                    }
            } else {
                // User is not authenticated
                Toast.makeText(context, "User is not authenticated. Please log in.", Toast.LENGTH_LONG).show()
            }
        }

        return view
    }

    private fun isValidCardNumber(cardNumber: String): Boolean {
        // Use a regular expression to validate a 16-digit card number
        val regex = "^[0-9]{16}$"
        return Pattern.matches(regex, cardNumber)
    }

    private fun isValidCvv(cvv: String): Boolean {
        // Use a regular expression to validate a 3-digit CVV
        val regex = "^[0-9]{3}$"
        return Pattern.matches(regex, cvv)
    }

    private fun isValidExpDate(expDate: String): Boolean {
        // Use a regular expression to validate digits for MM/YYYY
        val regex = "^(0[1-9]|1[0-2])/[0-9]{2}$"
        return Pattern.matches(regex, expDate)
    }
}
