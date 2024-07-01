package com.example.coffeecompass.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.adapter.ProductAdapter
import com.example.coffeecompass.adapter.ReviewAdapter
import com.example.coffeecompass.util.getProducts
import com.example.coffeecompass.util.getReviews
import kotlinx.coroutines.launch

class CoffeeShopDetailActivity : AppCompatActivity() {

    private lateinit var productAdapter: ProductAdapter
    private lateinit var reviewAdapter: ReviewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coffee_shop_detail)

        val coffeeShopId = intent.getStringExtra("coffeeShopId") ?: return

        productAdapter = ProductAdapter(listOf())
        reviewAdapter = ReviewAdapter(listOf())

        val productsRecyclerView: RecyclerView = findViewById(R.id.products_recycler_view)
        productsRecyclerView.layoutManager = LinearLayoutManager(this)
        productsRecyclerView.adapter = productAdapter

        val reviewsRecyclerView: RecyclerView = findViewById(R.id.reviews_recycler_view)
        reviewsRecyclerView.layoutManager = LinearLayoutManager(this)
        reviewsRecyclerView.adapter = reviewAdapter

        lifecycleScope.launch {
            val products = getProducts(coffeeShopId)
            productAdapter.updateData(products)

            val reviews = getReviews(coffeeShopId)
            reviewAdapter.updateData(reviews)
        }
    }
}
