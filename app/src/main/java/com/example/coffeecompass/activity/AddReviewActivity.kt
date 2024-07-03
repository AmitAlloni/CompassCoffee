package com.example.coffeecompass.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeecompass.R
import com.example.coffeecompass.model.Review
import com.example.coffeecompass.repository.ReviewsRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.*

class AddReviewActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var reviewsRepository: ReviewsRepository

    private lateinit var coffeeShopNameAutoCompleteTextView: AutoCompleteTextView
    private lateinit var ratingBar: RatingBar
    private lateinit var priceRadioGroup: RadioGroup
    private lateinit var flavorRadioGroup: RadioGroup
    private lateinit var reviewBodyEditText: EditText
    private lateinit var selectedImageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var submitReviewButton: Button
    private lateinit var progressBar: ProgressBar
    private lateinit var textViewSavingReview: TextView

    private var selectedImageUri: Uri? = null
    private var selectedCoffeeShopName: String? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val REQUEST_IMAGE_CAPTURE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_review)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()
        reviewsRepository = ReviewsRepository()

        coffeeShopNameAutoCompleteTextView = findViewById(R.id.autoCompleteTextViewCoffeeShopName)
        ratingBar = findViewById(R.id.ratingBar)
        priceRadioGroup = findViewById(R.id.priceRadioGroup)
        flavorRadioGroup = findViewById(R.id.flavorRadioGroup)
        reviewBodyEditText = findViewById(R.id.editTextReviewBody)
        selectedImageView = findViewById(R.id.imageViewSelectedImage)
        selectImageButton = findViewById(R.id.buttonSelectImage)
        submitReviewButton = findViewById(R.id.buttonSubmitReview)
        progressBar = findViewById(R.id.progressBar)
        textViewSavingReview = findViewById(R.id.textViewSavingReview)

        selectImageButton.setOnClickListener {
            showImagePickerOptions()
        }

        submitReviewButton.setOnClickListener {
            validateAndSubmitReview()
        }

        fetchCoffeeShopNames()
    }

    private fun fetchCoffeeShopNames() {
        firestore.collection("coffeeShops").get()
            .addOnSuccessListener { result ->
                val coffeeShopNames = result.map { it.getString("name") ?: "" }
                setupAutoCompleteTextView(coffeeShopNames)
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error fetching coffee shop names: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupAutoCompleteTextView(coffeeShopNames: List<String>) {
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, coffeeShopNames)
        coffeeShopNameAutoCompleteTextView.setAdapter(adapter)
        coffeeShopNameAutoCompleteTextView.threshold = 1

        coffeeShopNameAutoCompleteTextView.setOnItemClickListener { _, _, position, _ ->
            selectedCoffeeShopName = adapter.getItem(position)
        }
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Profile Image")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> takePhotoFromCamera()
                1 -> chooseImageFromGallery()
            }
        }
        builder.show()
    }

    private fun takePhotoFromCamera() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        }
    }

    private fun chooseImageFromGallery() {
        val pickPhotoIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        pickPhotoIntent.type = "image/*"
        startActivityForResult(pickPhotoIntent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                PICK_IMAGE_REQUEST -> {
                    selectedImageUri = data?.data
                    selectedImageView.setImageURI(selectedImageUri)
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    selectedImageUri = getImageUriFromBitmap(bitmap)
                    selectedImageView.setImageURI(selectedImageUri)
                }
            }
        }
    }

    private fun getImageUriFromBitmap(bitmap: Bitmap): Uri {
        val bytes = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
        val path = MediaStore.Images.Media.insertImage(contentResolver, bitmap, "ProfileImage", null)
        return Uri.parse(path)
    }

    private fun validateAndSubmitReview() {
        val rating = ratingBar.rating
        val reviewBody = reviewBodyEditText.text.toString()
        val price = when (priceRadioGroup.checkedRadioButtonId) {
            R.id.price1 -> 1
            R.id.price2 -> 2
            R.id.price3 -> 3
            R.id.price4 -> 4
            R.id.price5 -> 5
            else -> 0
        }
        val flavor = when (flavorRadioGroup.checkedRadioButtonId) {
            R.id.flavorStrong -> "Strong"
            R.id.flavorMild -> "Mild"
            R.id.flavorDecaf -> "Decaf"
            else -> ""
        }

        if (selectedCoffeeShopName.isNullOrEmpty()) {
            showMessage("Please select a coffee shop name")
            return
        }

        if (rating == 0f) {
            showMessage("Please provide a rating")
            return
        }

        if (reviewBody.isEmpty()) {
            showMessage("Please write a review")
            return
        }

        if (selectedImageUri == null) {
            showMessage("Please upload an image")
            return
        }

        if (price == 0) {
            showMessage("Please select a price")
            return
        }

        if (flavor.isEmpty()) {
            showMessage("Please select a flavor")
            return
        }

        showLoading(true)
        submitReview(selectedCoffeeShopName!!, rating.toInt(), reviewBody, price, flavor)
    }

    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
            textViewSavingReview.visibility = View.VISIBLE
            submitReviewButton.isEnabled = false
        } else {
            progressBar.visibility = View.GONE
            textViewSavingReview.visibility = View.GONE
            submitReviewButton.isEnabled = true
        }
    }

    private fun submitReview(coffeeShopName: String, rating: Int, reviewBody: String, price: Int, flavor: String) {
        val reviewId = UUID.randomUUID().toString()
        val userId = auth.currentUser?.uid ?: ""

        selectedImageUri?.let { uri ->
            val storageRef = storage.reference.child("reviewImages/$reviewId.jpg")
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        val review = Review(
                            id = reviewId,
                            writer = userId,
                            coffeeShop = coffeeShopName,
                            date = com.google.firebase.Timestamp.now(),
                            rate = rating,
                            body = reviewBody,
                            photo = downloadUri.toString(),
                            price = price,
                            flavor = flavor
                        )
                        reviewsRepository.addReview(review) { success ->
                            if (success) {
                                updateUserReviews(reviewId)
                                updateCoffeeShopReviews(coffeeShopName, reviewId)
                            } else {
                                showMessage("Error submitting review")
                                showLoading(false)
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    showMessage("Error uploading image")
                    showLoading(false)
                }
        }
    }

    private fun updateUserReviews(reviewId: String) {
        val userId = auth.currentUser?.uid ?: return
        firestore.collection("users").document(userId)
            .update("reviews", com.google.firebase.firestore.FieldValue.arrayUnion(reviewId))
            .addOnSuccessListener {
                finish() // Go back to UserProfileActivity
            }
            .addOnFailureListener {
                showMessage("Error updating user reviews")
                showLoading(false)
            }
    }

    private fun updateCoffeeShopReviews(coffeeShopName: String, reviewId: String) {
        firestore.collection("coffeeShops")
            .whereEqualTo("name", coffeeShopName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val coffeeShopId = document.id
                    firestore.collection("coffeeShops").document(coffeeShopId)
                        .update("reviews", com.google.firebase.firestore.FieldValue.arrayUnion(reviewId))
                        .addOnFailureListener {
                            showMessage("Error updating coffee shop reviews")
                            showLoading(false)
                        }
                }
            }
            .addOnFailureListener {
                showMessage("Error finding coffee shop")
                showLoading(false)
            }
    }
}
