package com.example.coffeecompass.repository

import androidx.lifecycle.LiveData
import com.example.coffeecompass.AppDatabase
import com.example.coffeecompass.model.CoffeeShop

class CoffeeShopRepository(private val database: AppDatabase) {

    private val coffeeShopDao = database.coffeeShopDao()

    // Function to get all coffee shops from the database
    fun getAllCoffeeShops(): LiveData<List<CoffeeShop>> {
        return coffeeShopDao.getAll()
    }

}
