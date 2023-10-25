package com.example.kindly

import androidx.fragment.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.backend.CharityDB
import com.example.kindly.backend.CharityUserAdapter
import com.google.firebase.database.*

class CharitiesFragment : Fragment() {
    private lateinit var database: FirebaseDatabase
    private lateinit var charityAdapter: CharityUserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_charities, container, false)
        val rvCharities: RecyclerView = view.findViewById(R.id.rvCharities)
        rvCharities.layoutManager = LinearLayoutManager(requireContext())
        charityAdapter = CharityUserAdapter(ArrayList())
        rvCharities.adapter = charityAdapter

        database = FirebaseDatabase.getInstance()
        val charitiesRef = database.reference.child("charities")

        charityAdapter.setOnItemClickListener(object : CharityUserAdapter.OnItemClickListener {
            override fun onItemClick(charity: CharityDB) {
                // Handle item click here
                val charityViewUserFragment = CharityViewUser.newInstance(charity)
                parentFragmentManager.beginTransaction()
                    .replace(R.id.frame_container, charityViewUserFragment)
                    .addToBackStack(null)
                    .commit()
            }
        })

        charitiesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val charityList = ArrayList<CharityDB>()
                for (charitySnapshot in snapshot.children) {
                    val charity = charitySnapshot.getValue(CharityDB::class.java)
                    if (charity != null) {
                        charityList.add(charity)
                    }
                }

                charityAdapter.updateData(charityList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle errors here
            }
        })

        return view
    }
}
