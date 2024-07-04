package com.example.coffeecompass.repository

import android.util.Log
import com.example.coffeecompass.model.CloudCoffeeShop
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class CoffeeShopRepository() {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun fetchAllCoffeeShops(): List<CloudCoffeeShop> {
        return try {
            val snapshot = firestore.collection("coffeeShops").get().await()
            val coffeeShops = snapshot.toObjects(CloudCoffeeShop::class.java)
            Log.d("CoffeeShopRepository", "Fetched ${coffeeShops.size} coffee shops")
            coffeeShops
        } catch (e: Exception) {
            Log.e("CoffeeShopRepository", "Error fetching coffee shops", e)
            emptyList()
        }
    }

    suspend fun getCoffeeShopById(id: String): CloudCoffeeShop? {
        return try {
            val document = firestore.collection("coffeeShops").document(id).get().await()
            document.toObject(CloudCoffeeShop::class.java)
        } catch (e: Exception) {
            null
        }
    }

}
