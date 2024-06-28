package com.example.coffeecompass

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import android.widget.ImageView
import androidx.activity.ComponentActivity

class SplashActivity : ComponentActivity() {
    private val SPLASH_TIME_OUT: Long = 3000 // 3 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        // Apply fade-in animation to the logo
        val logoImageView: ImageView = findViewById(R.id.logoImageView)
        val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        logoImageView.startAnimation(fadeIn)

        Handler(Looper.getMainLooper()).postDelayed({
            // Start MainActivity after the splash screen
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}
