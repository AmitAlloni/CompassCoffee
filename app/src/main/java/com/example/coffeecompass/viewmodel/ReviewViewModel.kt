package com.example.coffeecompass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.repository.ReviewRepository
import kotlinx.coroutines.launch

class ReviewViewModel : ViewModel() {

    private val reviewRepository = ReviewRepository()
    private val _reviews = MutableLiveData<List<Review>>()
    val reviews: LiveData<List<Review>> get() = _reviews
    private val _selectedReview = MutableLiveData<Review?>()
    val selectedReview: LiveData<Review?> get() = _selectedReview

    init {
        fetchReviews()
    }

    private fun fetchReviews() {
        viewModelScope.launch {
            val reviews = reviewRepository.fetchAllReviews()
            _reviews.postValue(reviews)
        }
    }

    fun getReviewById(reviewId: String): LiveData<Review?> {
        return reviewRepository.getReviewById(reviewId)
    }

    fun deleteReview(reviewId: String) {
        viewModelScope.launch {
            reviewRepository.deleteReview(reviewId)
            _selectedReview.postValue(null)
        }
    }
}
