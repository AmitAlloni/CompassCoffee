package com.example.coffeecompass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.coffeecompass.model.CoffeeShop
import com.example.coffeecompass.repository.CoffeeShopRepository

class CoffeeShopViewModel : ViewModel() {
    private val repository = CoffeeShopRepository()

    private val _coffeeShops = MutableLiveData<List<CoffeeShop>>()
    val coffeeShops: LiveData<List<CoffeeShop>> get() = _coffeeShops

    init {
        fetchCoffeeShops()
    }

    private fun fetchCoffeeShops() {
        repository.getCoffeeShops().observeForever { coffeeShops ->
            _coffeeShops.value = coffeeShops
        }
    }
}
