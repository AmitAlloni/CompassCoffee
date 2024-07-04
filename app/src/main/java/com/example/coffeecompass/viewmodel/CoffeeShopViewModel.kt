package com.example.coffeecompass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeecompass.model.CloudCoffeeShop
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.repository.CoffeeShopRepository
import com.example.coffeecompass.repository.ReviewRepository
import kotlinx.coroutines.launch

class CoffeeShopViewModel : ViewModel() {

    private val coffeeShopRepository = CoffeeShopRepository()
    private val reviewRepository = ReviewRepository()
    private val _coffeeShops = MutableLiveData<List<CloudCoffeeShop>>()
    val coffeeShops: LiveData<List<CloudCoffeeShop>> get() = _coffeeShops

    private val _selectedCoffeeShop = MutableLiveData<CloudCoffeeShop?>()
    val selectedCoffeeShop: LiveData<CloudCoffeeShop?> get() = _selectedCoffeeShop

    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews

    init {
        fetchCoffeeShops()
    }

    fun fetchCoffeeShops() {
        viewModelScope.launch {
            val coffeeShops = coffeeShopRepository.fetchAllCoffeeShops()
            _coffeeShops.postValue(coffeeShops)
        }
    }

    fun getCoffeeShopById(id: String) {
        viewModelScope.launch {
            val coffeeShop = coffeeShopRepository.getCoffeeShopById(id)
            _selectedCoffeeShop.postValue(coffeeShop)
        }
    }

    fun getReviewsByCoffeeShopId(coffeeShopId: String) {
        viewModelScope.launch {
            val reviews = reviewRepository.getReviewsByCoffeeShopId(coffeeShopId)
            reviews.observeForever {
                _reviews.postValue(it)
            }
        }
    }


}
