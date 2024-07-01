package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coffeecompass.R
import com.example.coffeecompass.viewmodel.CoffeeShopViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {

    private val viewModel: CoffeeShopViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        lifecycleScope.launch {
            val dataLoaded = viewModel.loadDataFromJson("coffeeshops.json", applicationContext)

            // Minimum delay to show splash screen for a reasonable time
            delay(2000)

            if (dataLoaded) {
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            } else {
                // Handle the case where data loading failed
            }
        }
    }
}
