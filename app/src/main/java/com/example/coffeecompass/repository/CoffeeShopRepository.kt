package com.example.coffeecompass.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.coffeecompass.room.AppDatabase
import com.example.coffeecompass.model.CloudCoffeeShop
import com.example.coffeecompass.model.LocalCoffeeShop
import com.example.coffeecompass.util.convertToLocalCoffeeShops
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CoffeeShopRepository() {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun fetchAllCoffeeShops(): List<CloudCoffeeShop> {
        return try {
            val snapshot = firestore.collection("coffeeShops").get().await()
            snapshot.toObjects(CloudCoffeeShop::class.java)
        } catch (e: Exception) {
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

    suspend fun insertCoffeeShopToFirestore(coffeeShop: CloudCoffeeShop) {
        try {
            firestore.collection("coffeeShops")
                .document(coffeeShop.id)
                .set(coffeeShop)
                .await()
        } catch (e: Exception) {
            Log.e("CoffeeShopRepository", "Error inserting coffee shop to Firestore", e)
        }
    }
}
