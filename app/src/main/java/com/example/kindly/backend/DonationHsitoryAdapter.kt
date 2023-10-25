package com.example.kindly.backend

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kindly.R

class DonationHistoryAdapter(private val donationList: List<DonationDB>) :
    RecyclerView.Adapter<DonationHistoryAdapter.DonationViewHolder>() {

    class DonationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val charityNameTextView: TextView = itemView.findViewById(R.id.TvCharityName)
        val amountTextView: TextView = itemView.findViewById(R.id.tvAmount)
        val dateTextView: TextView = itemView.findViewById(R.id.tvDate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DonationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_donation_history, parent, false)
        return DonationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: DonationViewHolder, position: Int) {
        val currentDonation = donationList[position]
        holder.charityNameTextView.text = currentDonation.charityName // Use charityName here
        holder.amountTextView.text = "${currentDonation.donationAmount} USD"
        holder.dateTextView.text = formatDonationDate(currentDonation.donationDate)
    }

    override fun getItemCount(): Int {
        return donationList.size
    }

    private fun formatDonationDate(date: String): String {
        // Assuming that the date format is "DD/MM/YY HH:mm:ss"
        val parts = date.split(" ")
        if (parts.isNotEmpty()) {
            val datePart = parts[0]
            val dateParts = datePart.split("/")
            if (dateParts.size == 3) {
                val day = dateParts[0]
                val month = dateParts[1]
                val year = dateParts[2].substring(2) // Extract last 2 digits of the year
                return "$day/$month/$year"
            }
        }
        return date // Return the original date if the format is invalid
    }
}
