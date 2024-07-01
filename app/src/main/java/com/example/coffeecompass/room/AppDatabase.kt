package com.example.coffeecompass.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coffeecompass.model.LocalCoffeeShop
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.InputStreamReader

@Database(entities = [LocalCoffeeShop::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun coffeeShopDao(): CoffeeShopDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "coffee_shop_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun AppDatabase.insertAllFromJson(context: Context, filename: String) {
            val inputStream = context.assets.open(filename)
            val jsonReader = InputStreamReader(inputStream)
            val coffeeShopType = object : TypeToken<List<LocalCoffeeShop>>() {}.type
            val coffeeShops: List<LocalCoffeeShop> = Gson().fromJson(jsonReader, coffeeShopType)
            this.coffeeShopDao().insertAll(coffeeShops)
        }
    }
}
