package com.example.kindly.backend

data class Chat(
    val sender: String = "",
    val message: String = "",
    val timestamp: Long
){
    constructor() : this("", "", 0L) // This is the no-argument constructor
}
