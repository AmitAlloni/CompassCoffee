package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coffeecompass.R
import com.example.coffeecompass.repository.CoffeeShopRepository
import com.example.coffeecompass.room.AppDatabase
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val coffeeShopRepository by lazy { CoffeeShopRepository(AppDatabase.getDatabase(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            try {
                // Synchronize coffee shops
                coffeeShopRepository.synchronizeCoffeeShops()
                Log.d("SplashActivity", "Data synchronized successfully")

                // Navigate to HomeActivity
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            } catch (e: Exception) {
                Log.e("SplashActivity", "Error during synchronization", e)
            }
        }
    }
}
