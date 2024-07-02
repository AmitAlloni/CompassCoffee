package com.example.coffeecompass.util

import com.example.coffeecompass.model.CloudCoffeeShop
import com.example.coffeecompass.model.LocalCoffeeShop

// Convert from Local to Cloud
fun localToCloud(localCoffeeShop: LocalCoffeeShop): CloudCoffeeShop {
    return CloudCoffeeShop(
        id = localCoffeeShop.id,
        name = localCoffeeShop.name,
        address = localCoffeeShop.address,
        rate = localCoffeeShop.rate,
        imageUrl = localCoffeeShop.imageUrl
    )
}

// Convert from Cloud to Local
fun convertToLocalCoffeeShops(cloudCoffeeShops: List<CloudCoffeeShop>): List<LocalCoffeeShop> {
    return cloudCoffeeShops.map { cloudCoffeeShop ->
        LocalCoffeeShop(
            id = cloudCoffeeShop.id,
            name = cloudCoffeeShop.name,
            address = cloudCoffeeShop.address,
            rate = cloudCoffeeShop.rate,
            imageUrl = cloudCoffeeShop.imageUrl
        )
    }
}