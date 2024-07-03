package com.example.coffeecompass.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
    private lateinit var reviewBodyEditText: EditText
    private lateinit var selectedImageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var submitReviewButton: Button

    private var selectedImageUri: Uri? = null

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
        reviewBodyEditText = findViewById(R.id.editTextReviewBody)
        selectedImageView = findViewById(R.id.imageViewSelectedImage)
        selectImageButton = findViewById(R.id.buttonSelectImage)
        submitReviewButton = findViewById(R.id.buttonSubmitReview)

        selectImageButton.setOnClickListener {
            showImagePickerOptions()
        }

        submitReviewButton.setOnClickListener {
            submitReview()
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
        coffeeShopNameAutoCompleteTextView.threshold = 1 // Start showing suggestions after one character
    }

    private fun showImagePickerOptions() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Select Profile Image")
        builder.setItems(options) { dialog, which ->
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

    private fun submitReview() {
        val coffeeShopName = coffeeShopNameAutoCompleteTextView.text.toString()
        val rating = ratingBar.rating.toInt()
        val reviewBody = reviewBodyEditText.text.toString()

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
                            photo = downloadUri.toString()
                        )
                        reviewsRepository.addReview(review) { success ->
                            if (success) {
                                updateUserReviews(reviewId)
                            }
                        }
                    }
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
    }
}
