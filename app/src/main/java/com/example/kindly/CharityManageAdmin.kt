package com.example.kindly

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kindly.backend.CharityDB
import com.example.kindly.backend.CharityAdapter
import com.example.kindly.databinding.ActivityCharityManageAdminBinding
import com.google.firebase.database.*

class CharityManageAdmin : AppCompatActivity() {
    private lateinit var binding: ActivityCharityManageAdminBinding
    private lateinit var database: FirebaseDatabase
    private lateinit var charityAdapter: CharityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCharityManageAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance()
        val charitiesRef = database.reference.child("charities")

        charityAdapter = CharityAdapter(emptyList(), emptyList()) // Provide empty lists
        binding.rvCharities.layoutManager = LinearLayoutManager(this)
        binding.rvCharities.adapter = charityAdapter

        charitiesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val charityList = mutableListOf<CharityDB>()
                val charityKeys = mutableListOf<String>()

                for (charitySnapshot in snapshot.children) {
                    val charityData = charitySnapshot.getValue(CharityDB::class.java)
                    if (charityData != null) {
                        charityList.add(charityData)
                        charityKeys.add(charitySnapshot.key ?: "")
                    }
                }

                charityAdapter.updateData(charityList, charityKeys)
                charityAdapter.notifyDataSetChanged() // Call notifyDataSetChanged() here
            }


            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@CharityManageAdmin, "Database error: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun addcharity(view: View) {
        val intent = Intent(this, AddCharity::class.java)
        startActivity(intent)
    }
}

