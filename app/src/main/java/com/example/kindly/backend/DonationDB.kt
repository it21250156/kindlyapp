package com.example.kindly.backend

data class DonationDB(
    val userId: String = "", // The user's ID
    val charityName: String = "",
    val userName: String = "", // You need to fetch the user's name
    val donationAmount: Double = 0.0, // Store the amount as a double
    val donationDate: String = "",
    val checked: Boolean = false // Store the checkbox state as a boolean
)

