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
fun generateUniqueId(): String {
    return UUID.randomUUID().toString()
}

val coffeeShop = CloudCoffeeShop(generateUniqueId(), "Coffee Shop Name2", "Address2", 4.5f, "https://firebasestorage.googleapis.com/v0/b/coffeecompass-1700d.appspot.com/o/ic_coffee_shops_color.png?alt=media&token=10dd11f0-e683-4b24-8539-6c360b81d31b", arrayListOf(), arrayListOf())

val coffeeShops = listOf(
    CloudCoffeeShop(
        FirestoreHelper.generateUniqueId(), "Central Perk", "90 Bedford St, New York, NY",
        4.8f, "https://example.com/image1.jpg",
        arrayListOf(), listOf(
            Product(FirestoreHelper.generateUniqueId(), "Latte", 3.5f),
            Product(FirestoreHelper.generateUniqueId(), "Cappuccino", 3.0f),
            Product(FirestoreHelper.generateUniqueId(), "Espresso", 2.5f),
            Product(FirestoreHelper.generateUniqueId(), "Mocha", 3.75f)
        )
    ),
    CloudCoffeeShop(
        FirestoreHelper.generateUniqueId(), "Blue Bottle Coffee", "300 Webster St, Oakland, CA",
        4.7f, "https://example.com/image2.jpg",
        arrayListOf(), listOf(
            Product(FirestoreHelper.generateUniqueId(), "Cold Brew", 4.0f),
            Product(FirestoreHelper.generateUniqueId(), "Americano", 3.25f),
            Product(FirestoreHelper.generateUniqueId(), "Macchiato", 3.5f),
            Product(FirestoreHelper.generateUniqueId(), "Flat White", 3.75f)
        )
    ),
    CloudCoffeeShop(
        FirestoreHelper.generateUniqueId(), "Starbucks Reserve Roastery", "1124 Pike St, Seattle, WA",
        4.6f, "https://example.com/image3.jpg",
        arrayListOf(), listOf(
            Product(FirestoreHelper.generateUniqueId(), "Reserve Cold Brew", 5.0f),
            Product(FirestoreHelper.generateUniqueId(), "Nitro Cold Brew", 4.75f),
            Product(FirestoreHelper.generateUniqueId(), "Clover Brewed Coffee", 4.5f),
            Product(FirestoreHelper.generateUniqueId(), "Affogato", 6.0f)
        )
    ),
    CloudCoffeeShop(
        FirestoreHelper.generateUniqueId(), "Intelligentsia Coffee", "53 E Randolph St, Chicago, IL",
        4.5f, "https://example.com/image4.jpg",
        arrayListOf(), listOf(
            Product(FirestoreHelper.generateUniqueId(), "Black Cat Espresso", 3.0f),
            Product(FirestoreHelper.generateUniqueId(), "Honey Badger Latte", 3.75f),
            Product(FirestoreHelper.generateUniqueId(), "Turkish Latte", 3.5f),
            Product(FirestoreHelper.generateUniqueId(), "Chai Latte", 3.25f)
        )
    ),
    CloudCoffeeShop(
        FirestoreHelper.generateUniqueId(), "Philz Coffee", "3101 24th St, San Francisco, CA",
        4.9f, "https://example.com/image5.jpg",
        arrayListOf(), listOf(
            Product(FirestoreHelper.generateUniqueId(), "Mint Mojito Iced Coffee", 4.25f),
            Product(FirestoreHelper.generateUniqueId(), "Tesora", 3.75f),
            Product(FirestoreHelper.generateUniqueId(), "Jacob's Wonderbar", 4.0f),
            Product(FirestoreHelper.generateUniqueId(), "Mocha Tesora", 4.5f)
        )
    ),
    CloudCoffeeShop(
        FirestoreHelper.generateUniqueId(), "Stumptown Coffee Roasters", "128 SW 3rd Ave, Portland, OR",
        4.7f, "https://example.com/image6.jpg",
        arrayListOf(), listOf(
            Product(FirestoreHelper.generateUniqueId(), "Hair Bender", 4.0f),
            Product(FirestoreHelper.generateUniqueId(), "Cold Brew", 4.5f),
            Product(FirestoreHelper.generateUniqueId(), "Nitro Cold Brew", 4.75f),
            Product(FirestoreHelper.generateUniqueId(), "Latte", 3.5f)
        )
    ),
    CloudCoffeeShop(
        FirestoreHelper.generateUniqueId(), "Bluebird Coffee Shop", "72 E 1st St, New York, NY",
        4.6f, "https://example.com/image7.jpg",
        arrayListOf(), listOf(
            Product(FirestoreHelper.generateUniqueId(), "Flat White", 3.75f),
            Product(FirestoreHelper.generateUniqueId(), "Matcha Latte", 4.0f),
            Product(FirestoreHelper.generateUniqueId(), "Iced Latte", 4.25f),
            Product(FirestoreHelper.generateUniqueId(), "Americano", 3.0f)
        )
    ),
    CloudCoffeeShop(
        FirestoreHelper.generateUniqueId(), "The Coffee Bean & Tea Leaf", "8735 W 3rd St, Los Angeles, CA",
        4.5f, "https://example.com/image8.jpg",
        arrayListOf(), listOf(
            Product(FirestoreHelper.generateUniqueId(), "Ice Blended Coffee", 4.25f),
            Product(FirestoreHelper.generateUniqueId(), "Caramel Latte", 4.0f),
            Product(FirestoreHelper.generateUniqueId(), "Chai Latte", 3.75f),
            Product(FirestoreHelper.generateUniqueId(), "Vanilla Latte", 4.0f)
        )
    )
)