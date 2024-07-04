package com.example.coffeecompass.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.activity.ViewReviewActivity
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.viewmodel.UserViewModel

class ReviewAdapter(private var reviews: List<Review>, private val lifecycleOwner: LifecycleOwner) :
    RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_review, parent, false)
        return ReviewViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviews[position]
        holder.bind(review)
    }

    override fun getItemCount(): Int = reviews.size

    fun updateData(newReviews: List<Review>) {
        reviews = newReviews
        notifyDataSetChanged()
    }

    inner class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val writerTextView: TextView = itemView.findViewById(R.id.reviewWriter)
        private val bodyTextView: TextView = itemView.findViewById(R.id.reviewBody)
        private val reviewRatingTextView: TextView = itemView.findViewById(R.id.reviewRating)

        fun bind(review: Review) {
            val userViewModel = ViewModelProvider(lifecycleOwner as AppCompatActivity).get(UserViewModel::class.java)
            userViewModel.getUserById(review.writer)
            userViewModel.user.observe(lifecycleOwner, Observer { user ->
                writerTextView.text = user?.userName ?: "Unknown"
            })

            bodyTextView.text = review.body
            reviewRatingTextView.text = review.rate.toString()

            itemView.setOnClickListener {
                val intent = Intent(itemView.context, ViewReviewActivity::class.java).apply {
                    putExtra("reviewId", review.id)
                }
                itemView.context.startActivity(intent)
            }
        }
    }
}
