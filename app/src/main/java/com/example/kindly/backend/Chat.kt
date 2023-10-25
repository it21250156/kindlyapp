package com.example.kindly.backend

data class Chat(
    val sender: String = "",
    val message: String = "",
    val timestamp: Long
)
