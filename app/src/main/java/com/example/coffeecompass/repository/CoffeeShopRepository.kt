package com.example.coffeecompass.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.coffeecompass.model.CoffeeShop
import com.google.firebase.database.*

class CoffeeShopRepository {
    private val database: DatabaseReference = FirebaseDatabase.getInstance().reference.child("coffeeShops")

    fun getCoffeeShops(): LiveData<List<CoffeeShop>> {
        val data = MutableLiveData<List<CoffeeShop>>()

        database.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val coffeeShops = mutableListOf<CoffeeShop>()
                for (dataSnapshot in snapshot.children) {
                    val coffeeShop = dataSnapshot.getValue(CoffeeShop::class.java)
                    if (coffeeShop != null) {
                        coffeeShops.add(coffeeShop)
                    }
                }
                data.value = coffeeShops
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        return data
    }
}
