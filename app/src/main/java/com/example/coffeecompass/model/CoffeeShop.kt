package com.example.coffeecompass.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "coffee_shops")
data class CoffeeShop(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "address") val address: String,
    @ColumnInfo(name = "rate") val rate: Float,
    @ColumnInfo(name = "imageUrl") val imageUrl: String,
)
