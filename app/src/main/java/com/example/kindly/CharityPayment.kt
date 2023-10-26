package com.example.kindly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.kindly.backend.DonationDB
import com.example.kindly.backend.PaymentMethodDB
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*
import com.google.firebase.firestore.FirebaseFirestore

class CharityPayment : Fragment() {

    private lateinit var charityNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var mobileNoTextView: TextView
    private lateinit var amountEditText: EditText
    private lateinit var recurringCheckBox: CheckBox
    private lateinit var donateButton: Button
    private lateinit var paymentMethodSpinner: Spinner

    private val userId: String = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    private val userName: String = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
    private val donationsRef = FirebaseDatabase.getInstance().getReference("donations")
    private val paymentMethodsCollection = FirebaseFirestore.getInstance().collection("payment_methods")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_charity_payment, container, false)

        charityNameTextView = view.findViewById(R.id.charityName)
        emailTextView = view.findViewById(R.id.email)
        mobileNoTextView = view.findViewById(R.id.mobileno)
        amountEditText = view.findViewById(R.id.EtAmount)
        recurringCheckBox = view.findViewById(R.id.recuring)
        donateButton = view.findViewById(R.id.btnDonate)
        paymentMethodSpinner = view.findViewById(R.id.paymentMethod)

        val charityName = arguments?.getString("charityName")
        val charityEmail = arguments?.getString("charityEmail")
        val charityMobile = arguments?.getString("charityMobile")

        charityNameTextView.text = charityName
        emailTextView.text = charityEmail
        mobileNoTextView.text = charityMobile

        // Fetch payment methods for the current user and populate the spinner
        fetchPaymentMethodsForUser()

        donateButton.setOnClickListener {
            donateToCharity(charityName ?: "", charityEmail ?: "", charityMobile ?: "")
        }

        return view
    }

    private fun fetchPaymentMethodsForUser() {
        // Filter payment methods for the current user
        paymentMethodsCollection
            .whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                if (!result.isEmpty) {
                    val paymentMethods = result.toObjects(PaymentMethodDB::class.java)
                    // Now you have payment methods for the current user, you can set up your spinner.
                    setupSpinner(paymentMethods)
                } else {
                    // Handle the case where no payment methods are found for the user.
                }
            }
            .addOnFailureListener { exception ->
                // Handle the error case.
            }
    }

    private fun setupSpinner(paymentMethods: List<PaymentMethodDB>) {
        val cardNumbers = paymentMethods.map { it.cardNumber }

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, cardNumbers)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        paymentMethodSpinner.adapter = adapter
    }

    private fun donateToCharity(charityName: String, charityEmail: String, charityMobile: String) {
        val donationAmountText = amountEditText.text.toString()
        val selectedPaymentMethod = paymentMethodSpinner.selectedItem.toString()

        if (donationAmountText.isEmpty()) {
            amountEditText.error = "Amount is required."
            return
        }

        // Convert the donation amount text to a double
        val donationAmount = donationAmountText.toDouble()

        // Generate a unique donation ID
        val donationId = donationsRef.push().key ?: ""

        // Get the current date and time
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val date = dateFormat.format(Calendar.getInstance().time)

        // Create a DonationDB object with corrected charityName and userName
        val donationData = DonationDB(
            userId,
            charityName,  // Corrected: Charity name should be here
            userName,     // Corrected: User name should be here
            donationAmount,
            date,
            recurringCheckBox.isChecked,
            selectedPaymentMethod // Include selected payment method
        )

        // Insert the donation data into the Firebase Realtime Database
        donationsRef.child(donationId).setValue(donationData)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // The data was successfully inserted into the database
                    Toast.makeText(context, "Donation successful!", Toast.LENGTH_SHORT).show()

                    // Clear the amount EditText and CheckBox
                    amountEditText.text.clear()
                    recurringCheckBox.isChecked = false

                    // Navigate back to the previous fragment
                    val fragmentManager = requireActivity().supportFragmentManager

                    // Navigate to the ThankYouFragment
                    fragmentManager.beginTransaction()
                        .replace(R.id.frame_container, ThankYouFragment())
                        .addToBackStack(null)
                        .commit()
                } else {
                    // Handle the case when the insertion was not successful
                    Toast.makeText(context, "Donation failed. Please try again.", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
