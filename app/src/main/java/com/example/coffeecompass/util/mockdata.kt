package com.example.coffeecompass.util

import com.example.coffeecompass.model.CloudCoffeeShop
import com.example.coffeecompass.model.Product
import com.example.coffeecompass.model.Review
import java.util.UUID


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

fun generateUniqueId(): String {
    return UUID.randomUUID().toString()
}

val coffeeShop = CloudCoffeeShop(generateUniqueId(), "Coffee Shop Name2", "Address2", 4.5f, "https://firebasestorage.googleapis.com/v0/b/coffeecompass-1700d.appspot.com/o/ic_coffee_shops_color.png?alt=media&token=10dd11f0-e683-4b24-8539-6c360b81d31b", arrayListOf(), arrayListOf())

