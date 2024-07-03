package com.example.coffeecompass.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.model.Review

class ReviewForUserAdapter(private val reviews: List<Review>, private val showViewMore: Boolean) : RecyclerView.Adapter<ReviewForUserAdapter.ReviewForUserAdapterHolder>() {

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
                // Navigate to a new activity to view all reviews
            }
        } else {
            val review = reviews[position]
            holder.coffeeShopTextView.text = review.coffeeShop
            holder.ratingTextView.text = review.rate.toString()
            holder.commentTextView.text = review.body
        }
    }

    override fun getItemCount(): Int {
        return if (showViewMore && reviews.size > 3) 4 else reviews.size
    }

    class ReviewForUserAdapterHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val coffeeShopTextView: TextView = itemView.findViewById(R.id.textViewCoffeeShop)
        val ratingTextView: TextView = itemView.findViewById(R.id.textViewRating)
        val commentTextView: TextView = itemView.findViewById(R.id.textViewComment)
    }
}
