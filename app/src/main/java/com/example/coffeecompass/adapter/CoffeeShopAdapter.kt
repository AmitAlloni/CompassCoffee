package com.example.coffeecompass.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.model.CoffeeShop
import com.squareup.picasso.Picasso

class CoffeeShopAdapter(private var coffeeShops: List<CoffeeShop>) :
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
        Picasso.get().load(coffeeShop.imageUrl).into(holder.imageView)
        holder.tvCoffeeShopName.text = coffeeShop.name
        holder.tvCoffeeShopRating.text = "Rating: ${coffeeShop.rating}"
        holder.tvCoffeeShopPrice.text = "Price: ${coffeeShop.price}"
        holder.tvCoffeeShopAddress.text = "Address: ${coffeeShop.address}"
    }

    override fun getItemCount() = coffeeShops.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newCoffeeShops: List<CoffeeShop>) {
        coffeeShops = newCoffeeShops
        notifyDataSetChanged()
    }
}
