package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.adapter.ReviewAdapter
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.viewmodel.ReviewViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ReviewsActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var searchCoffeeNameTextView: AutoCompleteTextView
    private lateinit var searchFlavorTextView: AutoCompleteTextView
    private lateinit var searchWriterTextView: AutoCompleteTextView
    private lateinit var sortByRatingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reviews)

        searchCoffeeNameTextView = findViewById(R.id.searchCoffeeNameTextView)
        searchFlavorTextView = findViewById(R.id.searchFlavorTextView)
        searchWriterTextView = findViewById(R.id.searchWriterTextView)
        sortByRatingButton = findViewById(R.id.sortByRatingButton)

        reviewAdapter = ReviewAdapter(emptyList(), this)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewReviews)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = reviewAdapter

        // Observe LiveData and update RecyclerView adapter
        viewModel.reviews.observe(this, Observer { reviews ->
            reviewAdapter.updateData(reviews)
            setupAutoComplete(reviews)
        })

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener(this)

        // Set up search and sorting
        setupSearch()
        setupSorting()

        val reviewId = intent.getStringExtra("REVIEW_ID")
        reviewId?.let {
            viewModel.getReviewById(it).observe(this, Observer { review ->
                review?.let { displaySingleReview(it) }
            })
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> {
                startActivity(Intent(this, HomeActivity::class.java))
                return true
            }
            R.id.navigation_coffee_shops -> {
                startActivity(Intent(this, CoffeeShopActivity::class.java))
                return true
            }
            R.id.navigation_reviews -> {
                // Stay in the same activity
                return true
            }
            R.id.navigation_profile -> {
                startActivity(Intent(this, UserProfileActivity::class.java))
                return true
            }
            else -> return false
        }
    }

    private fun setupAutoComplete(reviews: List<Review>) {
        val coffeeNames = reviews.map { it.coffeeShop }.distinct()
        val flavors = reviews.map { it.flavor }.distinct()
        val writers = reviews.map { it.writer }.distinct()

        val coffeeNameAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, coffeeNames)
        val flavorAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, flavors)
        val writerAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, writers)

        searchCoffeeNameTextView.setAdapter(coffeeNameAdapter)
        searchFlavorTextView.setAdapter(flavorAdapter)
        searchWriterTextView.setAdapter(writerAdapter)
    }

    private fun setupSearch() {
        searchCoffeeNameTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterReviews()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        searchFlavorTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterReviews()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        searchWriterTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterReviews()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterReviews() {
        val coffeeName = searchCoffeeNameTextView.text.toString()
        val flavor = searchFlavorTextView.text.toString()
        val writer = searchWriterTextView.text.toString()

        val filteredList = viewModel.reviews.value?.filter {
            (coffeeName.isEmpty() || it.coffeeShop.contains(coffeeName, ignoreCase = true)) &&
                    (flavor.isEmpty() || it.flavor.contains(flavor, ignoreCase = true)) &&
                    (writer.isEmpty() || it.writer.contains(writer, ignoreCase = true))
        }
        filteredList?.let { reviewAdapter.updateData(it) }
    }

    private fun setupSorting() {
        sortByRatingButton.setOnClickListener {
            val sortedList = viewModel.reviews.value?.sortedByDescending { it.rate }
            sortedList?.let { reviewAdapter.updateData(it) }
        }
    }

    private fun displaySingleReview(review: Review) {
        // Display the review details on the screen
        // Assuming you have TextViews or other UI elements to display the review details
        // e.g., coffeeShopNameTextView.text = review.coffeeShop
    }
}
