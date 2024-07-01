package com.example.coffeecompass.model

data class Product(
    val id: String,
    val name: String,
    val price: Float
)

data class CloudCoffeeShop(
    val id: String = "",
    val name: String = "",
    val address: String = "",
    val rate: Float = 0.0f,
    val imageUrl: String = "",
    val reviews: ArrayList<String> = arrayListOf(),
    val products: ArrayList<Product> = arrayListOf()
)
