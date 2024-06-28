package com.example.coffeecompass

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

data class CoffeeShop(val name: String, val rating: Float, val price: String, val address: String, val imageResId: Int)

class CoffeeShopAdapter(private val coffeeShops: List<CoffeeShop>) :
    RecyclerView.Adapter<CoffeeShopAdapter.CoffeeShopViewHolder>() {

    class CoffeeShopViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val tvCoffeeShopName: TextView = itemView.findViewById(R.id.tvCoffeeShopName)
        val tvCoffeeShopRating: TextView = itemView.findViewById(R.id.tvCoffeeShopRating)
        val tvCoffeeShopPrice: TextView = itemView.findViewById(R.id.tvCoffeeShopPrice)
        val tvCoffeeShopAddress: TextView = itemView.findViewById(R.id.tvCoffeeShopAddress)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeShopViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_coffee_shop, parent, false)
        return CoffeeShopViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: CoffeeShopViewHolder, position: Int) {
        val coffeeShop = coffeeShops[position]
        holder.imageView.setImageResource(coffeeShop.imageResId)
        holder.tvCoffeeShopName.text = coffeeShop.name
        holder.tvCoffeeShopRating.text = "Rating: ${coffeeShop.rating}"
        holder.tvCoffeeShopPrice.text = "Price: ${coffeeShop.price}"
        holder.tvCoffeeShopAddress.text = "Address: ${coffeeShop.address}"
    }

    override fun getItemCount() = coffeeShops.size
}
