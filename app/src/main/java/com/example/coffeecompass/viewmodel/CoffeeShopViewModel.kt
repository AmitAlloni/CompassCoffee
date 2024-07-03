package com.example.coffeecompass.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeecompass.model.CloudCoffeeShop
import com.example.coffeecompass.room.AppDatabase
import com.example.coffeecompass.model.LocalCoffeeShop
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.repository.CoffeeShopRepository
import com.example.coffeecompass.repository.ReviewsRepository
import kotlinx.coroutines.launch

class CoffeeShopViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: CoffeeShopRepository
    private val allCoffeeShops: LiveData<List<LocalCoffeeShop>>
    private val reviewsRepository = ReviewsRepository()

    init {
        val database = AppDatabase.getDatabase(application)
        repository = CoffeeShopRepository(database)
        allCoffeeShops = repository.getAllCoffeeShops()
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
