package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeecompass.R
import com.example.coffeecompass.model.UserSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val emailEditText = findViewById<EditText>(R.id.editTextEmail)
        val passwordEditText = findViewById<EditText>(R.id.editTextPassword)
        val usernameEditText = findViewById<EditText>(R.id.editTextUserName)
        val profileImageUrlEditText = findViewById<EditText>(R.id.editTextProfileImageUrl)
        //Todo: Change it to upload image and upload to storage
        val signUpButton = findViewById<Button>(R.id.buttonSignUp)

        signUpButton.setOnClickListener {
            val email = emailEditText.text.toString().trim()
            val password = passwordEditText.text.toString().trim()
            val username = usernameEditText.text.toString().trim()
            val profileImageUrl = profileImageUrlEditText.text.toString().trim()
            //Todo: make function that deal with upload image and save url
            if (email.isNotEmpty() && password.isNotEmpty() && username.isNotEmpty() && profileImageUrl.isNotEmpty()) {
                createUser(email, password, username, profileImageUrl)
            }
        }
    }

    private fun createUser(email: String, password: String, username: String, profileImageUrl: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    user?.let {
                        val uid = it.uid
                        val userData = hashMapOf(
                            "username" to username,
                            "profileImageUrl" to profileImageUrl,
                            "followers" to arrayListOf<String>(),
                            "following" to arrayListOf<String>(),
                            "reviews" to arrayListOf<String>(),
                            "userSettings" to UserSettings() // Assuming UserSettings is a data class
                        )

                        firestore.collection("users").document(uid)
                            .set(userData)
                            .addOnSuccessListener {
                                Toast.makeText(this, "User created successfully", Toast.LENGTH_SHORT).show()
                                startActivity(Intent(this, HomeActivity::class.java))
                            }
                            .addOnFailureListener {
                                Toast.makeText(this, "Error creating user", Toast.LENGTH_SHORT).show()
                            }
                    }
                } else {
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
