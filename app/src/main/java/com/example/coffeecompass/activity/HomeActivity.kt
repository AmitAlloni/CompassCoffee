package com.example.coffeecompass.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.adapter.CoffeeShopAdapter
import com.example.coffeecompass.viewmodel.CoffeeShopViewModel

class HomeActivity : ComponentActivity() {

    private val viewModel: CoffeeShopViewModel by viewModels()
    private lateinit var coffeeShopAdapter: CoffeeShopAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        coffeeShopAdapter = CoffeeShopAdapter(listOf())

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = coffeeShopAdapter

        viewModel.coffeeShops.observe(this, Observer { coffeeShops ->
            coffeeShopAdapter.updateData(coffeeShops)
        })
    }
}
