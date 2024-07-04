package com.example.coffeecompass.activity

import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.adapter.ProductAdapter
import com.example.coffeecompass.adapter.ReviewAdapter
import com.example.coffeecompass.model.CloudCoffeeShop
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.viewmodel.CoffeeShopViewModel
import com.example.coffeecompass.viewmodel.UserViewModel
import com.squareup.picasso.Picasso

class CoffeeShopDetailActivity : AppCompatActivity() {

    private val viewModel: CoffeeShopViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    private lateinit var productAdapter: ProductAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    private lateinit var coffeeShopNameTextView: TextView
    private lateinit var coffeeShopAddressTextView: TextView
    private lateinit var coffeeShopRateTextView: TextView
    private lateinit var coffeeShopImageView: ImageView
    private lateinit var likeButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coffee_shop_detail)

        coffeeShopNameTextView = findViewById(R.id.coffeeShopName)
        coffeeShopAddressTextView = findViewById(R.id.coffeeShopAddressTextView)
        coffeeShopRateTextView = findViewById(R.id.coffeeShopRateTextView)
        coffeeShopImageView = findViewById(R.id.coffeeShopDetailImageView)
        likeButton = findViewById(R.id.likeButton)

        val coffeeShopID = intent.getStringExtra("coffeeShopID")
        coffeeShopID?.let { id ->
            viewModel.getCoffeeShopById(id)
            viewModel.getReviewsByCoffeeShopId(id)
        }

        productAdapter = ProductAdapter(emptyList())
        reviewAdapter = ReviewAdapter(emptyList(), this)

        val productsRecyclerView: RecyclerView = findViewById(R.id.productsRecyclerView)
        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        productsRecyclerView.adapter = productAdapter

        val reviewsRecyclerView: RecyclerView = findViewById(R.id.reviewsRecyclerView)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewsRecyclerView.adapter = reviewAdapter

        viewModel.selectedCoffeeShop.observe(this, Observer { coffeeShop ->
            coffeeShop?.let {
                updateUI(it)
                updateLikeButton(it)
            }
        })

        viewModel.reviews.observe(this, Observer { reviews ->
            updateReviewsUI(reviews)
            Log.i("CoffeeShopId", coffeeShopID ?: "")
        })

        likeButton.setOnClickListener {
            viewModel.selectedCoffeeShop.value?.let { coffeeShop ->
                userViewModel.toggleLikeCoffeeShop(coffeeShop.id)
            }
        }
    }

    private fun updateUI(coffeeShop: CloudCoffeeShop) {
        coffeeShopNameTextView.text = coffeeShop.name
        coffeeShopAddressTextView.text = coffeeShop.address
        coffeeShopRateTextView.text = coffeeShop.rate.toString()
        Picasso.get().load(coffeeShop.imageUrl).into(coffeeShopImageView)
        productAdapter.updateData(coffeeShop.products)
    }

    private fun updateLikeButton(coffeeShop: CloudCoffeeShop) {
        userViewModel.user.observe(this, Observer { user ->
            if (user?.likedCoffeeShops?.contains(coffeeShop.id) == true) {
                likeButton.setImageResource(R.drawable.ic_like_selected)
            } else {
                likeButton.setImageResource(R.drawable.ic_like_unselected)
            }
        })
    }

    private fun updateReviewsUI(reviews: List<Review>) {
        // Update UI with reviews
        reviewAdapter.updateData(reviews)
    }
}
