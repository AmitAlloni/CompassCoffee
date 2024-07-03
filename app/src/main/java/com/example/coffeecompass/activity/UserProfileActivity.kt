package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.adapter.ReviewForUserAdapter
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.model.User
import com.example.coffeecompass.repository.ReviewsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class UserProfileActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private lateinit var reviewForUserAdapter: ReviewForUserAdapter
    private lateinit var reviewsRecyclerView: RecyclerView
    private lateinit var reviewsRepository: ReviewsRepository
    private lateinit var noReviewsTextView: TextView
    private lateinit var addReviewButton: Button
    private lateinit var profileImageView: ImageView
    private lateinit var usernameTextView: TextView
    private lateinit var followersTextView: TextView
    private lateinit var followingTextView: TextView
    private lateinit var userSettingsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        reviewsRepository = ReviewsRepository()

        profileImageView = findViewById(R.id.imageViewProfile)
        usernameTextView = findViewById(R.id.textViewGreeting)
        followersTextView = findViewById(R.id.textViewFollowers)
        followingTextView = findViewById(R.id.textViewFollowing)
        userSettingsButton = findViewById(R.id.buttonUserSettings)
        reviewsRecyclerView = findViewById(R.id.reviewsRecyclerView)
        noReviewsTextView = findViewById(R.id.textViewNoReviews)
        addReviewButton = findViewById(R.id.buttonAddReview)

        val uid = auth.currentUser?.uid
        uid?.let {
            loadUserProfile(it)
        }

        userSettingsButton.setOnClickListener {
            startActivity(Intent(this, UserSettingsActivity::class.java))
        }

        addReviewButton.setOnClickListener {
            val intent = Intent(this, AddReviewActivity::class.java)
            startActivity(intent)
        }

        noReviewsTextView.setOnClickListener {
            val intent = Intent(this, AddReviewActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        val uid = auth.currentUser?.uid
        uid?.let {
            loadUserProfile(it)
        }
    }

    private fun loadUserProfile(uid: String) {
        firestore.collection("users").document(uid).get()
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

    private fun loadUserReviews(reviewIds: List<String>) {
        lifecycleScope.launch {
            val reviews = reviewsRepository.getReviewsByIds(reviewIds)
            if (reviews.isEmpty()) {
                noReviewsTextView.visibility = View.VISIBLE
                reviewsRecyclerView.visibility = View.GONE
            } else {
                noReviewsTextView.visibility = View.GONE
                reviewsRecyclerView.visibility = View.VISIBLE
                val limitedReviews = if (reviews.size > 3) reviews.subList(0, 3) else reviews
                reviewForUserAdapter = ReviewForUserAdapter(limitedReviews, true)
                reviewsRecyclerView.layoutManager = LinearLayoutManager(this@UserProfileActivity, LinearLayoutManager.HORIZONTAL, false)
                reviewsRecyclerView.adapter = reviewForUserAdapter

                reviewForUserAdapter.setOnItemClickListener { review ->
                    if (review.id == "add_review") {
                        val intent = Intent(this@UserProfileActivity, AddReviewActivity::class.java)
                        startActivity(intent)
                    }
                }
            }
        }
    }
}
