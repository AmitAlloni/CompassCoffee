package com.example.coffeecompass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeecompass.model.CloudCoffeeShop
import com.example.coffeecompass.model.LocalCoffeeShop
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.repository.CoffeeShopRepository
import com.example.coffeecompass.repository.ReviewsRepository
import com.example.coffeecompass.room.AppDatabase
import kotlinx.coroutines.launch

class CoffeeShopViewModel(application: Application) : AndroidViewModel(application) {
    private val reviewsRepository = ReviewsRepository()
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

    fun getReviewsByCoffeeShopId(id: String): LiveData<List<Review>> {
        return reviewsRepository.getReviewsByCoffeeShopId(id)
    }

    fun insertCoffeeShopToFirestore(coffeeShop: CloudCoffeeShop) = viewModelScope.launch {
        repository.insertCoffeeShopToFirestore(coffeeShop)
    }
}
