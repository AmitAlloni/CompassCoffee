package com.example.coffeecompass.repository

import android.util.Log
import com.example.coffeecompass.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    suspend fun getUserById(userId: String): User? {
        return try {
            val document = firestore.collection("users").document(userId).get().await()
            document.toObject(User::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun saveUserToFirestore(user: FirebaseUser) {
        val userData = User(
            uid = user.uid,
            userName = user.displayName ?: "",
            email = user.email ?: "",
            profileImageUrl = user.photoUrl.toString()
        )
        //Todo: Save users settings into local database
        firestore.collection("users").document(user.uid)
            .set(userData)
            .addOnSuccessListener {
                Log.d("UserRepository", "User data saved successfully")
            }
            .addOnFailureListener { e ->
                Log.e("UserRepository", "Failed to save user data", e)
            }
    }

    fun getCurrentUser(): FirebaseUser? {
        return auth.currentUser
    }

    fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Registration successful
                    val user = auth.currentUser
                } else {
                    // Registration failed
                    Log.w("Registration", "createUserWithEmail:failure", task.exception)
                }
            }
    }

    fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Login successful
                    val user = auth.currentUser
                } else {
                    // Login failed
                    Log.w("Login", "signInWithEmail:failure", task.exception)
                }
            }
    }

    suspend fun updateUser(user: User) {
        try {
            firestore.collection("users").document(user.uid).set(user).await()
        } catch (e: Exception) {
            Log.e("UserRepository", "Failed to update user data", e)
        }
    }

    suspend fun toggleLikeCoffeeShop(userId: String, coffeeShopId: String) {
        val user = getUserById(userId)
        user?.let {
            if (it.likedCoffeeShops.contains(coffeeShopId)) {
                it.likedCoffeeShops.remove(coffeeShopId)
            } else {
                it.likedCoffeeShops.add(coffeeShopId)
            }
            updateUser(it)
        }
    }

}
