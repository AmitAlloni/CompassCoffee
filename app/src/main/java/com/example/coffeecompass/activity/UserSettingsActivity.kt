package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeecompass.R
import com.google.firebase.auth.FirebaseAuth

class UserSettingsActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)

        auth = FirebaseAuth.getInstance()

        val logoutButton = findViewById<Button>(R.id.buttonLogout)
        logoutButton.setOnClickListener {
            auth.signOut()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
