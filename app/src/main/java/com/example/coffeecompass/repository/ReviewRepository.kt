package com.example.coffeecompass.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.coffeecompass.model.CloudCoffeeShop
import com.example.coffeecompass.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await

class ReviewRepository {

    private val firestore = FirebaseFirestore.getInstance()

    suspend fun fetchAllReviews(): List<Review> {
        return try {
            val snapshot = firestore.collection("reviews").get().await()
            snapshot.toObjects(Review::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }

    private suspend fun fetchAllReviewsFromFirestore(reviewIds: List<String>): List<Review> {
        val reviews = mutableListOf<Review>()
        try {
            for (reviewId in reviewIds) {
                val document = firestore.collection("reviews").document(reviewId).get().await()
                document.toObject(Review::class.java)?.let {
                    reviews.add(it)
                }
            }
        } catch (e: Exception) {
            Log.e("ReviewsRepository", "Error fetching from Firestore", e)
        }
        return reviews
    }

    fun getReviewsByCoffeeShopId(coffeeShopId: String): LiveData<List<Review>> = liveData(Dispatchers.IO) {
        try {
            val document = firestore.collection("coffeeShops").document(coffeeShopId).get().await()
            val coffeeShop = document.toObject(CloudCoffeeShop::class.java)
            coffeeShop?.reviews?.let { reviewIds ->
                val reviews = fetchAllReviewsFromFirestore(reviewIds)
                emit(reviews)
            } ?: emit(emptyList<Review>())
        } catch (e: Exception) {
            Log.e("ReviewsRepository", "Error fetching reviews for coffee shop ID: $coffeeShopId", e)
            emit(emptyList<Review>())
        }
    }

    suspend fun getReviewsByIds(reviewIds: List<String>): List<Review> {
        return fetchAllReviewsFromFirestore(reviewIds)
    }

    fun addReview(review: Review, callback: (Boolean) -> Unit) {
        firestore.collection("reviews").document(review.id)
            .set(review)
            .addOnSuccessListener {
                callback(true)
            }
            .addOnFailureListener { e ->
                Log.e("ReviewsRepository", "Error adding review", e)
                callback(false)
            }
    }

    fun getReviewById(reviewId: String): LiveData<Review?> = liveData(Dispatchers.IO) {
        try {
            val document = firestore.collection("reviews").document(reviewId).get().await()
            val review = document.toObject(Review::class.java)
            emit(review)
        } catch (e: Exception) {
            emit(null)
        }
    }

    suspend fun deleteReview(reviewId: String) {
        try {
            firestore.collection("reviews").document(reviewId).delete().await()
        } catch (e: Exception) {
            // Handle exception if needed
        }
    }
}
