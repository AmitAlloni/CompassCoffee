package com.example.coffeecompass.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coffeecompass.model.CoffeeShop

@Dao
interface CoffeeShopDao {
    @Query("SELECT * FROM coffee_shops")
    fun getAll(): LiveData<List<CoffeeShop>>

    @Query("SELECT * FROM coffee_shops")
    fun getAllSync(): List<CoffeeShop> // Synchronous version for internal checks

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(coffeeShop: CoffeeShop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(coffeeShops: List<CoffeeShop>)

    @Query("DELETE FROM coffee_shops")
    fun deleteAll()
}
