package com.example.coffeecompass.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.model.LocalCoffeeShop
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CoffeeShopAdapter(
    private var coffeeShops: List<LocalCoffeeShop>,
    private val onCoffeeShopClick: (LocalCoffeeShop) -> Unit
) : RecyclerView.Adapter<CoffeeShopAdapter.CoffeeShopViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoffeeShopViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_coffee_shop, parent, false)
        return CoffeeShopViewHolder(itemView, onCoffeeShopClick)
    }

    override fun onBindViewHolder(holder: CoffeeShopViewHolder, position: Int) {
        val coffeeShop = coffeeShops[position]
        holder.bind(coffeeShop)
    }

    override fun getItemCount(): Int {
        return coffeeShops.size
    }

    fun updateData(newCoffeeShops: List<LocalCoffeeShop>) {
        coffeeShops = newCoffeeShops
        notifyDataSetChanged()
    }

    class CoffeeShopViewHolder(
        itemView: View,
        private val onCoffeeShopClick: (LocalCoffeeShop) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.coffeeShopImageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.coffeeShopNameTextView)
        private val addressTextView: TextView = itemView.findViewById(R.id.coffeeShopAddressTextView)
        private val rateTextView: TextView = itemView.findViewById(R.id.coffeeShopRateTextView)

        fun bind(coffeeShop: LocalCoffeeShop) {
            nameTextView.text = coffeeShop.name
            addressTextView.text = coffeeShop.address
            rateTextView.text = coffeeShop.rate.toString()

            // Load image using Picasso with logging
            val imageUrl = coffeeShop.imageUrl
            if (!imageUrl.isNullOrEmpty()) {
                Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.ic_coffee_shop_placeholder) // Placeholder image
                    .error(R.drawable.ic_error) // Error image
                    .into(imageView, object : Callback {
                        override fun onSuccess() {
                            Log.d("Picasso", "Image loaded successfully: $imageUrl")
                        }

                        override fun onError(e: Exception?) {
                            Log.e("Picasso", "Error loading image: $imageUrl", e)
                        }
                    })
            } else {
                imageView.setImageResource(R.drawable.ic_coffee_shop_placeholder)
            }

            itemView.setOnClickListener {
                onCoffeeShopClick(coffeeShop)
            }
        }
    }
}
