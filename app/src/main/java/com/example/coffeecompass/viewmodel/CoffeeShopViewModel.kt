package com.example.coffeecompass.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.coffeecompass.model.CoffeeShop
import com.example.coffeecompass.room.AppDatabase
import com.example.coffeecompass.room.CoffeeShopDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

class CoffeeShopViewModel(application: Application) : AndroidViewModel(application) {
    private val coffeeShopDao: CoffeeShopDao = AppDatabase.getDatabase(application).coffeeShopDao()
    private val allCoffeeShops: LiveData<List<CoffeeShop>> = coffeeShopDao.getAll()

    fun getAllCoffeeShops(): LiveData<List<CoffeeShop>> {
        return allCoffeeShops
    }

    fun insert(coffeeShop: CoffeeShop) = viewModelScope.launch(Dispatchers.IO) {
        coffeeShopDao.insert(coffeeShop)
    }

    fun insertAll(coffeeShops: List<CoffeeShop>) = viewModelScope.launch(Dispatchers.IO) {
        coffeeShopDao.insertAll(coffeeShops)
    }

    suspend fun loadDataFromJson(fileName: String, context: Context): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                if (coffeeShopDao.getAllSync().isEmpty()) { // Check if the database is empty
                    val inputStream = context.assets.open(fileName)
                    val json = InputStreamReader(inputStream).use { it.readText() }
                    val coffeeShopListType = object : TypeToken<List<CoffeeShop>>() {}.type
                    val coffeeShops: List<CoffeeShop> = Gson().fromJson(json, coffeeShopListType)
                    insertAll(coffeeShops)
                }
                true
            } catch (e: Exception) {
                Log.e("CoffeeShopViewModel", "Error loading data from JSON", e)
                false
            }
        }
    }
}
