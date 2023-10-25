package com.example.kindly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.backend.DonationAdapter
import com.example.kindly.backend.DonationDB
import com.example.kindly.backend.DonationHistoryAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class DonationHistory : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var donationAdapter: DonationHistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_donation_history, container, false)

        recyclerView = view.findViewById(R.id.rvDonations)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Fetch and populate the donations
        fetchDonations()

        return view
    }

    private fun fetchDonations() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        if (userId != null) {
            // Reference to the "donations" node in the Realtime Database
            val donationsRef = FirebaseDatabase.getInstance().getReference("donations")

            // Query to fetch donations for the current user
            val query = donationsRef.orderByChild("userId").equalTo(userId)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val donationList = mutableListOf<DonationDB>()

                    for (donationSnapshot in dataSnapshot.children) {
                        val donation = donationSnapshot.getValue(DonationDB::class.java)
                        donation?.let {
                            donationList.add(it)
                        }
                    }

                    // Create and set up the adapter with the donation data
                    donationAdapter = DonationHistoryAdapter(donationList)
                    recyclerView.adapter = donationAdapter
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    // Handle errors here
                }
            })
        } else {
            // User is not authenticated
            // Handle this condition as needed
        }
    }
}
