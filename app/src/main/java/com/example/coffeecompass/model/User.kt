package com.example.coffeecompass.model
import com.google.firebase.firestore.DocumentReference

data class User(
    val id: String = "",
    val name: String = "",
    val reviews: List<String > = emptyList()
)
