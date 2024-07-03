package com.example.coffeecompass.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.adapter.CoffeeShopAdapter
import com.example.coffeecompass.fragment.*
import com.example.coffeecompass.viewmodel.CoffeeShopViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView


class CoffeeShopActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val viewModel: CoffeeShopViewModel by viewModels()
    private lateinit var coffeeShopAdapter: CoffeeShopAdapter
    private lateinit var searchEditText: EditText
    private lateinit var sortByRatingButton: Button
    private lateinit var sortByReviewsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coffee_shop)

        searchEditText = findViewById(R.id.searchEditText)
        sortByRatingButton = findViewById(R.id.sortByRatingButton)
        sortByReviewsButton = findViewById(R.id.sortByReviewsButton)

        coffeeShopAdapter = CoffeeShopAdapter(listOf()) { coffeeShop ->
            val intent = Intent(this, CoffeeShopDetailActivity::class.java).apply {
                putExtra("coffeeShopID", coffeeShop.id)
            }
            startActivity(intent)
        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewCoffeeShops)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = coffeeShopAdapter

        // Observe LiveData and update RecyclerView adapter
        viewModel.coffeeShops.observe(this, Observer { coffeeShops ->
            coffeeShopAdapter.updateData(coffeeShops)
        })

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    startActivity(Intent(this, HomeActivity::class.java))
                    true
                }
                R.id.navigation_coffee_shops -> {
                    startActivity(Intent(this, CoffeeShopActivity::class.java))
                    true
                }
                R.id.navigation_reviews -> {
                    // Todo: Handle reviews click
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, UserProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
        // Set default fragment
        loadFragment(CoffeeShopsFragment())

        // Set up search and sorting
        setupSearch()
        setupSorting()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_home -> loadFragment(HomeFragment())
            R.id.navigation_coffee_shops -> loadFragment(CoffeeShopsFragment())
            R.id.navigation_reviews -> loadFragment(ReviewsFragment())
            R.id.navigation_profile -> loadFragment(ProfileFragment())
            else -> return false
        }
        return true
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                // Filter the coffee shop list based on the search input
                val filteredList = viewModel.coffeeShops.value?.filter {
                    it.name.contains(s.toString(), ignoreCase = true)
                }
                filteredList?.let { coffeeShopAdapter.updateData(it) }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun setupSorting() {
        sortByRatingButton.setOnClickListener {
            val sortedList = viewModel.coffeeShops.value?.sortedByDescending { it.rate }
            sortedList?.let { coffeeShopAdapter.updateData(it) }
        }

        sortByReviewsButton.setOnClickListener {
            val sortedList = viewModel.coffeeShops.value?.sortedByDescending { it.reviews.size }
            sortedList?.let { coffeeShopAdapter.updateData(it) }
        }
    }
}
