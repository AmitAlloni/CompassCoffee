package com.example.coffeecompass.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeecompass.model.CoffeeShop
import com.example.coffeecompass.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class CoffeeShopRepository {
    private val db = FirebaseFirestore.getInstance()
    private val coffeeShposRef = db.collection("coffee_shpos")

    suspend fun getCoffeeShops(): LiveData<List<CoffeeShop>> {
        val data = MutableLiveData<List<CoffeeShop>>()

        try {
            val result = coffeeShposRef.get().await()
            val coffeeShops = mutableListOf<CoffeeShop>()
            for (document in result) {
                Log.d("CoffeeShopRepository", "Document data: ${document.data}")
//                val reviews = getReviews(document.id)
                val coffeeShop = document.toObject<CoffeeShop>()?.copy(
                    /*reviews = reviews*/
                )
                if (coffeeShop != null) {
                    coffeeShops.add(coffeeShop)
                }
            }
            withContext(Dispatchers.Main) {
                data.value = coffeeShops
            }
            Log.d("CoffeeShopRepository", "Fetched coffee shops: $coffeeShops")
        } catch (e: Exception) {
            Log.e("CoffeeShopRepository", "Error fetching coffee shops", e)
        }

        return data
    }

    /*private suspend fun getReviews(coffeeShopId: String): List<Review> {
        return try {
            val result = coffeeShposRef.document(coffeeShopId).collection("reviews").get().await()
            val reviews = result.documents.mapNotNull { document ->
                val reviewData = document.data
                if (reviewData != null) {
                    Review(
                        id = document.id,
                        writer = (reviewData["user_id"] as? DocumentReference)?.id ?: "",
                        coffeeShop = coffeeShopId,
                        date = reviewData["date"] as? com.google.firebase.Timestamp,
                        rate = (reviewData["rate"] as? Long)?.toInt() ?: 0,
                        flavor = (reviewData["flavor"] as? Long)?.toInt() ?: 0,
                        price = (reviewData["price"] as? Long)?.toInt() ?: 0,
                        photo = reviewData["photo"] as? String ?: "",
                        body = reviewData["body"] as? String ?: ""
                    )
                } else {
                    null
                }
            }
            Log.d("CoffeeShopRepository", "Fetched reviews for $coffeeShopId: $reviews")
            reviews
        } catch (e: Exception) {
            Log.e("CoffeeShopRepository", "Error fetching reviews for $coffeeShopId", e)
            emptyList()
        }
    }*/
}
