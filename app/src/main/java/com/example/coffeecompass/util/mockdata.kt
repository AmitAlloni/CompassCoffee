package com.example.coffeecompass.util

import com.example.coffeecompass.model.Product
import com.example.coffeecompass.model.Review

// Mock Data for Products
val mockProducts = listOf(
    Product(id = "prod1", name = "Cappuccino", price = 16.50f),
    Product(id = "prod2", name = "Americano", price = 15.00f)
)

// Mock Data for Reviews
val mockReviews = listOf(
    Review(id = "rev1", writer = "User1", coffeeShop = "shop1", rate = 4, flavor = 4, price = 4, body = "Great coffee!"),
    Review(id = "rev2", writer = "User2", coffeeShop = "shop1", rate = 5, flavor = 5, price = 5, body = "Strong coffee like coffee should be!")
)
