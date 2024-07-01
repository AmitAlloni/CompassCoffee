package com.example.coffeecompass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.coffeecompass.AppDatabase
import com.example.coffeecompass.model.CoffeeShop
import com.example.coffeecompass.repository.CoffeeShopRepository

class CoffeeShopViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CoffeeShopRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = CoffeeShopRepository(database)
    }

    fun getAllCoffeeShops(): LiveData<List<CoffeeShop>> {
        return repository.getAllCoffeeShops()
    }
}
