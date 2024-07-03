package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeecompass.R
import com.example.coffeecompass.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val profileImageView = findViewById<ImageView>(R.id.imageViewProfile)
        //Todo: use function
        val usernameTextView = findViewById<TextView>(R.id.textViewUsername)
        val reviewsTextView = findViewById<TextView>(R.id.textViewReviews)
        //Todo: use function
        val followersTextView = findViewById<TextView>(R.id.textViewFollowers)
        val followingTextView = findViewById<TextView>(R.id.textViewFollowing)
        val editProfileButton = findViewById<Button>(R.id.buttonEditProfile)

        val uid = auth.currentUser?.uid
        uid?.let {
            firestore.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    user?.let { userDetails ->
                        usernameTextView.text = userDetails.userName
                        followersTextView.text = "Followers: ${userDetails.followers.size}"
                        followingTextView.text = "Following: ${userDetails.following.size}"

                        //Todo: Load profile image using a library like Picasso or Glide
                        // Example: Picasso.get().load(userDetails.profileImageUrl).into(profileImageView)
                    }
                }
        }

        editProfileButton.setOnClickListener {
            startActivity(Intent(this, UserSettingsActivity::class.java))
        }
    }
}
