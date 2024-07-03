package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.coffeecompass.R
import com.example.coffeecompass.viewmodel.CoffeeShopViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    //Todo: remove app title from splash activity
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        auth = FirebaseAuth.getInstance()

        // Check if user is authenticated
        val currentUser = auth.currentUser
        if (currentUser != null) {
            lifecycleScope.launch {
                delay(2000) // Simulating a loading period
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            }
        } else {
            lifecycleScope.launch {
                delay(2000) // Simulating a loading period
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }
        }
    }
}
