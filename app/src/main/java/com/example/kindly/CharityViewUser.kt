package com.example.kindly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.example.kindly.backend.CharityDB

class CharityViewUser : Fragment() {
    private lateinit var charity: CharityDB

    companion object {
        fun newInstance(charity: CharityDB): CharityViewUser {
            val fragment = CharityViewUser()
            fragment.charity = charity
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_charity_view_user, container, false)

        val btnDonate: Button = view.findViewById(R.id.btnDonate)

        btnDonate.setOnClickListener {
            val charityPaymentFragment = CharityPayment()

            val bundle = Bundle()
            bundle.putString("charityName", charity.name)
            bundle.putString("charityEmail", charity.email)
            bundle.putString("charityMobile", charity.contact)
            charityPaymentFragment.arguments = bundle

            val fragmentManager = parentFragmentManager
            val transaction = fragmentManager.beginTransaction()
            transaction.replace(R.id.frame_container, charityPaymentFragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }

        val ivImage: ImageView = view.findViewById(R.id.ivImage)
        val tvCharityName: TextView = view.findViewById(R.id.tvCharityName)
        val tvDescription: TextView = view.findViewById(R.id.tvDescription)

        tvCharityName.text = charity.name
        tvDescription.text = charity.description

        // Load the image using Glide (you should include your Glide logic here)

        return view
    }
}
