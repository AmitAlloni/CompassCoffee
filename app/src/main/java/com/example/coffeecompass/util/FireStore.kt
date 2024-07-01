package com.example.coffeecompass.util

import com.example.coffeecompass.model.Product
import com.example.coffeecompass.model.Review
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

// Function to insert products into Firestore
suspend fun insertProducts(products: List<Product>) {
    withContext(Dispatchers.IO) {
        val db = FirebaseFirestore.getInstance()
        val productsCollection = db.collection("products")
        for (product in products) {
            productsCollection.document(product.id).set(product)
        }
    }
}

// Function to insert reviews into Firestore
suspend fun insertReviews(reviews: List<Review>) {
    withContext(Dispatchers.IO) {
        val db = FirebaseFirestore.getInstance()
        val reviewsCollection = db.collection("reviews")
        for (review in reviews) {
            reviewsCollection.document(review.id).set(review)
        }
    }
}

// Function to get products from Firestore
suspend fun getProducts(coffeeShopId: String): List<Product> {
    return withContext(Dispatchers.IO) {
        val db = FirebaseFirestore.getInstance()
        val documents = db.collection("products")
            .whereEqualTo("coffeeShopId", coffeeShopId)
            .get()
            .await()
        documents.map { it.toObject(Product::class.java) }
    }
}

// Function to get reviews from Firestore
suspend fun getReviews(coffeeShopId: String): List<Review> {
    return withContext(Dispatchers.IO) {
        val db = FirebaseFirestore.getInstance()
        val documents = db.collection("reviews")
            .whereEqualTo("coffeeShopId", coffeeShopId)
            .get()
            .await()
        documents.map { it.toObject(Review::class.java) }
    }
}

