package com.example.kindly.backend

// DonationDB data class with a paymentMethod field
data class DonationDB(
    val userId: String = "",
    val charityName: String = "",
    val userName: String = "",
    val donationAmount: Double = 0.0,
    val donationDate: String = "",
    val recurring: Boolean = false,
    val paymentMethod: String = ""
)


