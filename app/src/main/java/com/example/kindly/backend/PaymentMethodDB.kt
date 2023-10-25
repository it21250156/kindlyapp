package com.example.kindly.backend

data class PaymentMethodDB(
    val userId: String = "",
    val cardNumber: String = "",
    val cvv: String = "",
    val expDate: String = ""
)

