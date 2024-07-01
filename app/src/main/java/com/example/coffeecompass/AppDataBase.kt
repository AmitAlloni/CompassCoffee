package com.example.coffeecompass

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coffeecompass.model.CoffeeShop
import com.example.coffeecompass.room.CoffeeShopDao

@Database(entities = [CoffeeShop::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): CoffeeShopDao

    companion object AppDatabaseLocal {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

