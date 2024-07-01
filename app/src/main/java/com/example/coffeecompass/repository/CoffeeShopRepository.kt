package com.example.coffeecompass.repository

import android.content.Context
import android.util.Log
import com.example.coffeecompass.model.CoffeeShop
import com.example.coffeecompass.room.CoffeeShopDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.gson.Gson
import kotlinx.coroutines.withContext

class CoffeeShopRepository(private val coffeeShopDao: CoffeeShopDao, private val scope: CoroutineScope) {

    fun insert(coffeeShop: CoffeeShop) {
        scope.launch {
            coffeeShopDao.insert(coffeeShop)
            Log.d("Database", "Inserted coffee shop: $coffeeShop")
        }
    }

    fun insertAllFromJson(context: Context, fileName: String) {
        scope.launch(Dispatchers.IO) {
            val inputStream = context.assets.open(fileName)
            val json = inputStream.bufferedReader().use { it.readText() }
            val coffeeShops = Gson().fromJson(json, Array<CoffeeShop>::class.java).toList()

            coffeeShopDao.insertAll(coffeeShops)
            Log.d("Database", "Inserted ${coffeeShops.size} coffee shops from $fileName")

            // Query to verify insertion
            withContext(Dispatchers.Main) {
                coffeeShopDao.getAll().observeForever { list ->
                    Log.d("Database", "All Coffee Shops: $list")
                }
            }
        }
    }

    fun getAllCoffeeShops() {
        scope.launch {
            coffeeShopDao.getAll().observeForever { list ->
                Log.d("Database", "Retrieved ${list.size} coffee shops from the database")
                for (shop in list) {
                    Log.d("Database", shop.toString())
                }
            }
        }
    }
}

