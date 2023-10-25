package com.example.kindly.backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.R
import com.example.kindly.backend.DonationDB

class DonationAdapter(private val donationList: List<DonationDB>) :
    RecyclerView.Adapter<DonationAdapter.DonationViewHolder>() {

    class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val userNameTextView: TextView = itemView.findViewById(R.id.tvUserName)
        val amountTextView: TextView = itemView.findViewById(R.id.tvAmount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donations, parent, false)
        return DonationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val currentDonation = donationList[position]
        holder.userNameTextView.text = currentDonation.userName
        holder.amountTextView.text = "${currentDonation.donationAmount} USD"
    }

    override fun getItemCount(): Int {
        return donationList.size
    }
}
