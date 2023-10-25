package com.example.kindly

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.kindly.backend.CharityDB
import com.google.firebase.database.*

class CharityViewUser : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var tvCharityName: TextView
    private lateinit var tvDescription: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_charity_view_user, container, false)

        // Initialize your TextViews
        tvCharityName = view.findViewById(R.id.tvCharityName)
        tvDescription = view.findViewById(R.id.tvDescription)

        database = FirebaseDatabase.getInstance()
        val charityRef = database.reference.child("charities").child("charity_id") // Replace with the actual charity ID

        charityRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val charity = snapshot.getValue(CharityDB::class.java)
                if (charity != null) {
                    // Populate your TextViews with the charity data
                    tvCharityName.text = charity.name
                    tvDescription.text = charity.description
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })

        return view
    }
}
