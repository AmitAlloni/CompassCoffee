package com.example.coffeecompass

import android.app.Application
import com.example.coffeecompass.room.AppDatabase
import com.google.firebase.FirebaseApp

class CoffeeShopApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
        AppDatabase.getDatabase(this)
    }
}
