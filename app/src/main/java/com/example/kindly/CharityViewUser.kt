package com.example.kindly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.backend.CharityDB
import com.example.kindly.backend.DonationAdapter
import com.example.kindly.backend.DonationDB
import com.google.firebase.database.*

class CharityViewUser : Fragment() {
    private lateinit var charity: CharityDB
    private lateinit var donationAdapter: DonationAdapter
    private lateinit var rvCharities: RecyclerView
    private val donationsRef = FirebaseDatabase.getInstance().getReference("donations")

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
        rvCharities = view.findViewById(R.id.rvCharities)

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

        // Initialize the RecyclerView
        val layoutManager = LinearLayoutManager(context)
        rvCharities.layoutManager = layoutManager
        donationAdapter = DonationAdapter(emptyList()) // Initialize the adapter with an empty list
        rvCharities.adapter = donationAdapter

        // Retrieve and display donations for the specific charity
        val databaseReference = donationsRef.orderByChild("charityName").equalTo(charity.name)

        databaseReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val donations = mutableListOf<DonationDB>()
                for (donationSnapshot in snapshot.children) {
                    val donation = donationSnapshot.getValue(DonationDB::class.java)
                    donation?.let { donations.add(it) }
                }
                donationAdapter = DonationAdapter(donations)
                rvCharities.adapter = donationAdapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return view
    }
}
