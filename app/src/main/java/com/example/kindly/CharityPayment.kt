package com.example.kindly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

class CharityPayment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_charity_payment, container, false)

        val charityNameTextView: TextView = view.findViewById(R.id.charityName)
        val emailTextView: TextView = view.findViewById(R.id.email)
        val mobileNoTextView: TextView = view.findViewById(R.id.mobileno)

        val charityName = arguments?.getString("charityName")
        val charityEmail = arguments?.getString("charityEmail")
        val charityMobile = arguments?.getString("charityMobile")

        charityNameTextView.text = charityName
        emailTextView.text = charityEmail
        mobileNoTextView.text = charityMobile

        // Rest of your existing code...

        return view
    }
}
