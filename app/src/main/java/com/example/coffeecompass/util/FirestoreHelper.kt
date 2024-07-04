package com.example.coffeecompass.util

import com.example.coffeecompass.model.CloudCoffeeShop
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

object FirestoreHelper {

    private val firestore = FirebaseFirestore.getInstance()

    fun generateUniqueId(): String {
        return firestore.collection("temp").document().id
    }

    suspend fun insertCoffeeShops(coffeeShops: List<CloudCoffeeShop>) {
        coffeeShops.forEach { coffeeShop ->
            firestore.collection("coffeeShops").document(coffeeShop.id).set(coffeeShop).await()
        }
    }
}
