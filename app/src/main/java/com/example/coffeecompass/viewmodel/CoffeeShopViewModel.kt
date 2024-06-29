package com.example.coffeecompass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.coffeecompass.model.CoffeeShop
import com.example.coffeecompass.repository.CoffeeShopRepository
import kotlinx.coroutines.Dispatchers

class CoffeeShopViewModel : ViewModel() {
    private val repository = CoffeeShopRepository()

    val coffeeShops: LiveData<List<CoffeeShop>> = liveData(Dispatchers.IO) {
        val data = repository.getCoffeeShops()
        emitSource(data)
    }
}
