package com.example.coffeecompass.activity

import android.os.Bundle
import android.util.Log
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
import com.example.coffeecompass.model.Product
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.viewmodel.CoffeeShopViewModel
import com.squareup.picasso.Picasso

class CoffeeShopDetailActivity : AppCompatActivity() {

    private val viewModel: CoffeeShopViewModel by viewModels()

    private lateinit var productAdapter: ProductAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    private lateinit var coffeeShopNameTextView: TextView
    private lateinit var coffeeShopAddressTextView: TextView
    private lateinit var coffeeShopRateTextView: TextView
    private lateinit var coffeeShopImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coffee_shop_detail)

        coffeeShopNameTextView = findViewById(R.id.coffeeShopNameTextView)
        coffeeShopAddressTextView = findViewById(R.id.coffeeShopAddressTextView)
        coffeeShopRateTextView = findViewById(R.id.coffeeShopRateTextView)
        coffeeShopImageView = findViewById(R.id.coffeeShopDetailImageView)

        val coffeeShopID = intent.getStringExtra("coffeeShopID")
        coffeeShopID?.let { id ->
            viewModel.getCoffeeShopById(id)
            viewModel.getReviewsByCoffeeShopId(id)
        }

        productAdapter = ProductAdapter(emptyList())
        reviewAdapter = ReviewAdapter(emptyList())

        val productsRecyclerView: RecyclerView = findViewById(R.id.productsRecyclerView)
        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        productsRecyclerView.adapter = productAdapter

        val reviewsRecyclerView: RecyclerView = findViewById(R.id.reviewsRecyclerView)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewsRecyclerView.adapter = reviewAdapter

        viewModel.selectedCoffeeShop.observe(this, Observer { coffeeShop ->
            coffeeShop?.let { updateUI(it) }
        })

        viewModel.reviews.observe(this, Observer { reviews ->
            updateReviewsUI(reviews)
            Log.i("CoffeeShopId", coffeeShopID ?: "")
        })
    }

    private fun updateUI(coffeeShop: CloudCoffeeShop) {
        coffeeShopNameTextView.text = coffeeShop.name
        coffeeShopAddressTextView.text = coffeeShop.address
        coffeeShopRateTextView.text = coffeeShop.rate.toString()
        Picasso.get().load(coffeeShop.imageUrl).into(coffeeShopImageView)

        // Assuming you have methods to fetch the products and reviews
        fetchProducts(coffeeShop.id)
    }

    private fun fetchProducts(coffeeShopId: String) {
        // Implement fetching products from Firestore and update the adapter
        val products = listOf(
            Product("1", "Latte", 4.5f),
            Product("2", "Espresso", 3.0f)
        )
        productAdapter.updateData(products)
    }

    private fun updateReviewsUI(reviews: List<Review>) {
        // Update UI with reviews
        reviewAdapter.updateData(reviews)
    }
}
