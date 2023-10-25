package com.example.kindly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.kindly.fragments.AddPaymentMethod
import com.google.android.material.floatingactionbutton.FloatingActionButton


class SavedPaymentMethods : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_saved_payment_methods, container, false)

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
}
