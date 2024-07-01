package com.example.coffeecompass.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coffeecompass.model.LocalCoffeeShop

@Dao
interface CoffeeShopDao {
    @Query("SELECT * FROM coffee_shops")
    fun getAll(): LiveData<List<LocalCoffeeShop>>

    @Query("SELECT * FROM coffee_shops")
    fun getAllSync(): List<LocalCoffeeShop> // Synchronous version for internal checks

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coffeeShop: LocalCoffeeShop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(coffeeShops: List<LocalCoffeeShop>)

    @Query("DELETE FROM coffee_shops")
    fun deleteAll()
}
