package com.example.coffeecompass.model

import com.google.firebase.Timestamp


data class Review(
    val id: String = "",
    val writer: String  = "",
    val coffeeShop: String = "",
    val date: Timestamp? = null,
    val rate: Int = 0,
    val flavor: String = "",
    val price: Int = 0,
    val photo: String = "",
    val body: String = ""
)
