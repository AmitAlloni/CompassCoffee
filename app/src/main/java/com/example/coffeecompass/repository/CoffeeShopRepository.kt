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

class CoffeeShopRepository(private val localDb: AppDatabase) {

    private val firestore = FirebaseFirestore.getInstance()

    private suspend fun fetchAllCoffeeShopsFromFirestore(): List<CloudCoffeeShop> {
        val coffeeShops = mutableListOf<CloudCoffeeShop>()
        try {
            val result = firestore.collection("coffeeShops").get().await()
            for (document in result) {
                Log.d("CoffeeShopRepository", "Document ID: ${document.id}, Data: ${document.data}")
                val coffeeShop = document.toObject(CloudCoffeeShop::class.java)
                coffeeShops.add(coffeeShop)
            }
            Log.d("CoffeeShopRepository", "Fetched ${coffeeShops.size} coffee shops from Firestore")
        } catch (e: Exception) {
            Log.e("CoffeeShopRepository", "Error fetching from Firestore", e)
        }
        return coffeeShops
    }

    private suspend fun insertAllCoffeeShopsToLocalDb(coffeeShops: List<LocalCoffeeShop>) {
        withContext(Dispatchers.IO) {
            try {
                if (coffeeShops.isNotEmpty()) {
                    localDb.coffeeShopDao().insertAll(coffeeShops)
                    Log.d("CoffeeShopRepository", "Inserted ${coffeeShops.size} coffee shops into local DB")
                } else {
                    Log.w("CoffeeShopRepository", "No coffee shops to insert into local DB")
                }
            } catch (e: Exception) {
                Log.e("CoffeeShopRepository", "Error inserting to local DB", e)
            }
        }
    }

    suspend fun synchronizeCoffeeShops() {
        try {
            val cloudCoffeeShops = fetchAllCoffeeShopsFromFirestore()
            if (cloudCoffeeShops.isNotEmpty()) {
                val localCoffeeShops = convertToLocalCoffeeShops(cloudCoffeeShops)
                insertAllCoffeeShopsToLocalDb(localCoffeeShops)
            } else {
                Log.w("CoffeeShopRepository", "No coffee shops fetched from Firestore")
            }
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
