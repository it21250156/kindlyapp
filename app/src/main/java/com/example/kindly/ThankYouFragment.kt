package com.example.kindly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class ThankYouFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_thank_you, container, false)

        // Find the "View Donation History" button and set a click listener
        val btnViewDonationHistory = view.findViewById<Button>(R.id.btn_admin_home_charities)
        btnViewDonationHistory.setOnClickListener {
            // Navigate to the DonationHistory fragment
            val donationHistoryFragment = DonationHistory()
            val fragmentManager = parentFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frame_container, donationHistoryFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        return view
    }
}
