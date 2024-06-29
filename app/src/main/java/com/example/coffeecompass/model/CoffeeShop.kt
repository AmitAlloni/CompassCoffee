package com.example.coffeecompass.model


data class CoffeeShop(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val rate: Float = 0.0f,
    /*val reviews: List<Review> = emptyList(),*/
    val products: List<Map<String, Any>> = emptyList(),
    val imageURL: String = ""
)
