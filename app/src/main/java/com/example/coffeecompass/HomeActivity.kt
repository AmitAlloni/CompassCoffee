package com.example.coffeecompass

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        // Sample data for visited coffee shops
        val coffeeShops = listOf(
            CoffeeShop("Coffee House", 4.5f, "$10", "123 Main St", R.drawable.ic_coffee_shop_placeholder),
            CoffeeShop("Brewed Awakenings", 4.0f, "$12", "456 Elm St", R.drawable.ic_coffee_shop_placeholder),
            CoffeeShop("Cafe Latte", 4.7f, "$8", "789 Oak St", R.drawable.ic_coffee_shop_placeholder)
        )

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = CoffeeShopAdapter(coffeeShops)
    }
}
