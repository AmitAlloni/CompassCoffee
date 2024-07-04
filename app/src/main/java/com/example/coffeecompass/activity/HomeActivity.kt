package com.example.coffeecompass.activity

import android.content.Intent
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
import com.example.coffeecompass.adapter.HomeCoffeeShopAdapter
import com.example.coffeecompass.fragment.*
import com.example.coffeecompass.viewmodel.CoffeeShopViewModel
import com.example.coffeecompass.viewmodel.UserViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity(), BottomNavigationView.OnNavigationItemSelectedListener {

    private val viewModel: CoffeeShopViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()
    private lateinit var coffeeShopAdapter: HomeCoffeeShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)


        coffeeShopAdapter = HomeCoffeeShopAdapter(listOf(), { coffeeShop ->
            val intent = Intent(this, CoffeeShopDetailActivity::class.java).apply {
                putExtra("coffeeShopID", coffeeShop.id)
            }
            startActivity(intent)
        }, this, userViewModel)

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = coffeeShopAdapter

        viewModel.coffeeShops.observe(this, Observer { coffeeShops ->
            coffeeShopAdapter.updateData(coffeeShops)
        })

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    true
                }
                R.id.navigation_coffee_shops -> {
                    startActivity(Intent(this, CoffeeShopActivity::class.java))
                    true
                }
                R.id.navigation_reviews -> {
                    startActivity(Intent(this, ReviewsActivity::class.java))
                    true
                }
                R.id.navigation_profile -> {
                    startActivity(Intent(this, UserProfileActivity::class.java))
                    true
                }
                else -> false
            }
        }
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