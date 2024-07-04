package com.example.coffeecompass.fragment

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coffeecompass.R
import com.example.coffeecompass.adapter.ReviewAdapter
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.viewmodel.ReviewViewModel


class ReviewsFragment : Fragment() {

    private val viewModel: ReviewViewModel by viewModels()
    private lateinit var reviewAdapter: ReviewAdapter
    private lateinit var searchCoffeeNameTextView: AutoCompleteTextView
    private lateinit var searchFlavorTextView: AutoCompleteTextView
    private lateinit var searchWriterTextView: AutoCompleteTextView
    private lateinit var sortByRatingButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reviews, container, false)

        searchCoffeeNameTextView = view.findViewById(R.id.searchCoffeeNameTextView)
        searchFlavorTextView = view.findViewById(R.id.searchFlavorTextView)
        searchWriterTextView = view.findViewById(R.id.searchWriterTextView)
        sortByRatingButton = view.findViewById(R.id.sortByRatingButton)

        reviewAdapter = ReviewAdapter(emptyList(), this)

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewReviews)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = reviewAdapter

        // Observe LiveData and update RecyclerView adapter
        viewModel.reviews.observe(viewLifecycleOwner, Observer { reviews ->
            reviewAdapter.updateData(reviews)
            setupAutoComplete(reviews)
        })

        // Set up search and sorting
        setupSearch()
        setupSorting()

        return view
    }

    private fun setupAutoComplete(reviews: List<Review>) {
        val coffeeNames = reviews.map { it.coffeeShop }.distinct()
        val flavors = reviews.map { it.flavor }.distinct()
        val writers = reviews.map { it.writer }.distinct()

        val coffeeNameAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, coffeeNames)
        val flavorAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, flavors)
        val writerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, writers)

        searchCoffeeNameTextView.setAdapter(coffeeNameAdapter)
        searchFlavorTextView.setAdapter(flavorAdapter)
        searchWriterTextView.setAdapter(writerAdapter)
    }

    private fun setupSearch() {
        searchCoffeeNameTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterReviews()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        searchFlavorTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterReviews()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        searchWriterTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                filterReviews()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    private fun filterReviews() {
        val coffeeName = searchCoffeeNameTextView.text.toString()
        val flavor = searchFlavorTextView.text.toString()
        val writer = searchWriterTextView.text.toString()

        val filteredList = viewModel.reviews.value?.filter {
            (coffeeName.isEmpty() || it.coffeeShop.contains(coffeeName, ignoreCase = true)) &&
                    (flavor.isEmpty() || it.flavor.contains(flavor, ignoreCase = true)) &&
                    (writer.isEmpty() || it.writer.contains(writer, ignoreCase = true))
        }
        filteredList?.let { reviewAdapter.updateData(it) }
    }

    private fun setupSorting() {
        sortByRatingButton.setOnClickListener {
            val sortedList = viewModel.reviews.value?.sortedByDescending { it.rate }
            sortedList?.let { reviewAdapter.updateData(it) }
        }
    }
}
