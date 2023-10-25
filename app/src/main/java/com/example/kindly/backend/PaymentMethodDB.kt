package com.example.kindly.backend

data class PaymentMethodDB(
    val userId: String, // Add user ID field
    val cardNumber: String,
    val cvv: String,
    val expDate: String
)
