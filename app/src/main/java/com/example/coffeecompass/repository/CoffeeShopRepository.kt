package com.example.coffeecompass.repository

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.coffeecompass.room.AppDatabase
import com.example.coffeecompass.model.CloudCoffeeShop
import com.example.coffeecompass.model.LocalCoffeeShop
import com.example.coffeecompass.model.Product
import com.example.coffeecompass.util.convertToLocalCoffeeShops
import com.example.coffeecompass.util.mockProducts
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CoffeeShopRepository(private val localDb: AppDatabase) {

    private val firestore = FirebaseFirestore.getInstance()

    private suspend fun fetchAllCoffeeShopsFromFirestore(): List<CloudCoffeeShop> {
        val coffeeShops = mutableListOf<CloudCoffeeShop>()
        try {
            val result = firestore.collection("coffeeShops").get().await()
            for (document in result) {
                val coffeeShop = document.toObject(CloudCoffeeShop::class.java)
                coffeeShops.add(coffeeShop)
            }
        } catch (e: Exception) {
            Log.e("CoffeeShopRepository", "Error fetching from Firestore", e)
        }
        //insertProducts(mockProducts)
        return coffeeShops
    }

    private suspend fun insertAllCoffeeShopsToLocalDb(coffeeShops: List<LocalCoffeeShop>) {
        withContext(Dispatchers.IO) {
            try {
                localDb.coffeeShopDao().insertAll(coffeeShops)
            } catch (e: Exception) {
                Log.e("CoffeeShopRepository", "Error inserting to local DB", e)
            }
        }
    }

    // Function to insert products into Firestore
    private suspend fun insertProducts(products: List<Product>) {
        withContext(Dispatchers.IO) {
            val db = FirebaseFirestore.getInstance()
            val productsCollection = db.collection("products")
            for (product in products) {
                productsCollection.document(product.id).set(product)
            }
        }
    }

    suspend fun synchronizeCoffeeShops() {
        try {
            // Fetch from Firestore
            val cloudCoffeeShops = fetchAllCoffeeShopsFromFirestore()

            // Convert to local coffee shops using the converter function
            val localCoffeeShops = convertToLocalCoffeeShops(cloudCoffeeShops)

            // Insert into local DB
            insertAllCoffeeShopsToLocalDb(localCoffeeShops)
        } catch (e: Exception) {
            Log.e("CoffeeShopRepository", "Error synchronizing coffee shops", e)
        }
    }

    fun getAllCoffeeShops(): LiveData<List<LocalCoffeeShop>> {
        return localDb.coffeeShopDao().getAll()
    }

    fun getCoffeeShopById(id: String): LiveData<LocalCoffeeShop> {
        return localDb.coffeeShopDao().getById(id)
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
