package com.example.coffeecompass.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.coffeecompass.model.CoffeeShop

@Dao
interface CoffeeShopDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(coffeeShop: CoffeeShop)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(coffeeShops: List<CoffeeShop>)

    @Query("SELECT * FROM coffee_shops")
    fun getAll(): LiveData<List<CoffeeShop>>

    @Query("SELECT * FROM coffee_shops WHERE id = :id")
    fun getById(id: String): LiveData<CoffeeShop>

    @Delete
    fun delete(coffeeShop: CoffeeShop)
}
