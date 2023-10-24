package com.example.kindly.backend

data class CharityDB(
    val name: String,
    val address: String,
    val contact: String,
    val email: String,
    val description: String,
    val imageUri: String // You should store the URL of the image
)
