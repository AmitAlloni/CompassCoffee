package com.example.coffeecompass.activity

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.adapter.CoffeeShopAdapter
import com.example.coffeecompass.fragment.CoffeeShopsFragment
import com.example.coffeecompass.fragment.HomeFragment
import com.example.coffeecompass.fragment.ProfileFragment
import com.example.coffeecompass.fragment.ReviewsFragment
import com.example.coffeecompass.viewmodel.CoffeeShopViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val viewModel: CoffeeShopViewModel by viewModels()
    private lateinit var coffeeShopAdapter: CoffeeShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        coffeeShopAdapter = CoffeeShopAdapter(listOf())

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = coffeeShopAdapter

        // Observe LiveData and update RecyclerView adapter
        viewModel.coffeeShops.observe(this, Observer { coffeeShops ->
            coffeeShopAdapter.updateData(coffeeShops)
        })

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener(this)

        // Set default fragment
        loadFragment(HomeFragment())
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
}
