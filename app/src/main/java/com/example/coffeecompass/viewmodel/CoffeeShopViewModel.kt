package com.example.coffeecompass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeecompass.model.LocalCoffeeShop
import com.example.coffeecompass.repository.CoffeeShopRepository
import com.example.coffeecompass.room.AppDatabase
import kotlinx.coroutines.launch

class CoffeeShopViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: CoffeeShopRepository

    init {
        val localDb = AppDatabase.getDatabase(application)
        repository = CoffeeShopRepository(localDb)
        synchronizeCoffeeShops()
    }

    private fun synchronizeCoffeeShops() = viewModelScope.launch {
        repository.synchronizeCoffeeShops()
    }

    fun getAllCoffeeShops(): LiveData<List<LocalCoffeeShop>> {
        return repository.getAllCoffeeShops()
    }

    fun getCoffeeShopById(id: String): LiveData<LocalCoffeeShop> {
        return repository.getCoffeeShopById(id)
    }
}
