package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.model.User
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.adapter.ReviewForUserAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso

class UserProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var reviewForUserAdapter: ReviewForUserAdapter
    private lateinit var reviewsRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val profileImageView = findViewById<ImageView>(R.id.imageViewProfile)
        val usernameTextView = findViewById<TextView>(R.id.textViewGreeting)
        val followersTextView = findViewById<TextView>(R.id.textViewFollowers)
        val followingTextView = findViewById<TextView>(R.id.textViewFollowing)
        val userSettingsButton = findViewById<Button>(R.id.buttonUserSettings)
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView)

        val uid = auth.currentUser?.uid
        uid?.let {
            firestore.collection("users").document(it).get()
                .addOnSuccessListener { document ->
                    val user = document.toObject(User::class.java)
                    user?.let { userDetails ->
                        setUsername(usernameTextView, userDetails.userName)
                        setFollowersCount(followersTextView, userDetails.followers.size)
                        setFollowingCount(followingTextView, userDetails.following.size)
                        loadProfileImage(profileImageView, userDetails.profileImageUrl)
                        loadUserReviews(userDetails.reviews)
                    }
                }
        }

        userSettingsButton.setOnClickListener {
            startActivity(Intent(this, UserSettingsActivity::class.java))
        }
    }

    private fun setUsername(textView: TextView, username: String) {
        textView.text = "Hi, $username"
    }

    private fun setFollowersCount(textView: TextView, count: Int) {
        textView.text = "Followers: $count"
    }

    private fun setFollowingCount(textView: TextView, count: Int) {
        textView.text = "Following: $count"
    }

    private fun loadProfileImage(imageView: ImageView, imageUrl: String) {
        Picasso.get().load(imageUrl).placeholder(R.drawable.ic_profile).into(imageView)
    }

    private fun loadUserReviews(reviews: List<Review>) {
        val limitedReviews = if (reviews.size > 3) reviews.subList(0, 3) else reviews
        reviewForUserAdapter = ReviewForUserAdapter(limitedReviews, true)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        reviewsRecyclerView.adapter = reviewForUserAdapter
    }
}
