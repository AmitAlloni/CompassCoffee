package com.example.coffeecompass.model

import com.example.coffeecompass.util.FirestoreHelper.generateUniqueId

data class CloudCoffeeShop(
    var id: String = "",
    var name: String = "",
    var address: String = "",
    var rate: Float = 0.0f,
    var imageUrl: String = "",
    var reviews: List<String> = listOf(),
    var products: List<Product> = listOf()
)


