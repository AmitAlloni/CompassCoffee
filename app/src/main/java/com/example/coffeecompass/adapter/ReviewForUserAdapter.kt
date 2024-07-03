package com.example.coffeecompass.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.model.Review

class ReviewForUserAdapter(private val reviews: List<Review>, private val showViewMore: Boolean) : RecyclerView.Adapter<ReviewForUserAdapter.ReviewForUserAdapterHolder>() {

    private var onItemClickListener: ((Review) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewForUserAdapterHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review_for_user, parent, false)
        return ReviewForUserAdapterHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewForUserAdapterHolder, position: Int) {
        if (showViewMore && position == reviews.size) {
            holder.coffeeShopTextView.text = "View More"
            holder.ratingTextView.visibility = View.GONE
            holder.commentTextView.visibility = View.GONE
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(Review(id = "add_review"))
            }
        } else {
            val review = reviews[position]
            holder.coffeeShopTextView.text = review.coffeeShop
            holder.ratingTextView.text = review.rate.toString()
            holder.priceTextView.text = review.price.toString()
            holder.flavorTextView.text = review.flavor
            holder.commentTextView.text = review.body
            holder.itemView.setOnClickListener {
                onItemClickListener?.invoke(review)
            }
        }
    }

    override fun getItemCount(): Int {
        return if (showViewMore && reviews.size > 3) 4 else reviews.size
    }

    fun setOnItemClickListener(listener: (Review) -> Unit) {
        onItemClickListener = listener
    }

    class ReviewForUserAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coffeeShopTextView: TextView = itemView.findViewById(R.id.textViewCoffeeShop)
        val ratingTextView: TextView = itemView.findViewById(R.id.textViewRating)
        val priceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
        val flavorTextView: TextView = itemView.findViewById(R.id.textViewFlavor)
        val commentTextView: TextView = itemView.findViewById(R.id.textViewComment)
    }
}



