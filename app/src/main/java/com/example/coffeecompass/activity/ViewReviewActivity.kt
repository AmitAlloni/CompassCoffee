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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import java.io.ByteArrayOutputStream

class ViewReviewActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var review: Review

    private lateinit var coffeeShopNameAutoCompleteTextView: AutoCompleteTextView
    private lateinit var ratingBar: RatingBar
    private lateinit var priceRadioGroup: RadioGroup
    private lateinit var flavorRadioGroup: RadioGroup
    private lateinit var reviewBodyEditText: EditText
    private lateinit var reviewImageView: ImageView
    private lateinit var selectImageButton: Button
    private lateinit var saveReviewButton: Button
    private lateinit var editButton: Button

    private var selectedImageUri: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val REQUEST_IMAGE_CAPTURE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_review)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        coffeeShopNameAutoCompleteTextView = findViewById(R.id.autoCompleteTextViewCoffeeShopName)
        ratingBar = findViewById(R.id.ratingBar)
        priceRadioGroup = findViewById(R.id.priceRadioGroup)
        flavorRadioGroup = findViewById(R.id.flavorRadioGroup)
        reviewBodyEditText = findViewById(R.id.editTextReviewBody)
        reviewImageView = findViewById(R.id.imageViewReviewImage)
        selectImageButton = findViewById(R.id.buttonSelectImage)
        saveReviewButton = findViewById(R.id.buttonSaveReview)
        editButton = Button(this).apply {
            text = "Edit Review"
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(350, 0, 16, 0)
            }
        }

        val reviewId = intent.getStringExtra("REVIEW_ID")
        loadReview(reviewId)

        selectImageButton.setOnClickListener {
            showImagePickerOptions()
        }

        saveReviewButton.setOnClickListener {
            saveReview()
        }
    }

    private fun loadReview(reviewId: String?) {
        if (reviewId == null) {
            Toast.makeText(this, "Error loading review", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        firestore.collection("reviews").document(reviewId).get()
            .addOnSuccessListener { document ->
                review = document.toObject(Review::class.java) ?: return@addOnSuccessListener
                displayReview(review)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error loading review", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    private fun displayReview(review: Review) {
        coffeeShopNameAutoCompleteTextView.setText(review.coffeeShop)
        ratingBar.rating = review.rate.toFloat()
        priceRadioGroup.check(getPriceRadioButtonId(review.price))
        flavorRadioGroup.check(getFlavorRadioButtonId(review.flavor))
        reviewBodyEditText.setText(review.body)
        Picasso.get().load(review.photo).placeholder(R.drawable.ic_profile).into(reviewImageView)

        if (review.writer == auth.currentUser?.uid) {
            // Add edit button to the layout
            val rootLayout = findViewById<LinearLayout>(R.id.rootLayout)
            rootLayout.addView(editButton)
            editButton.setOnClickListener {
                enableEditing()
            }
        }
    }

    private fun enableEditing() {
        coffeeShopNameAutoCompleteTextView.isEnabled = true
        ratingBar.setIsIndicator(false)
        enableRadioGroupChildren(priceRadioGroup, true)
        enableRadioGroupChildren(flavorRadioGroup, true)
        reviewBodyEditText.isEnabled = true
        selectImageButton.visibility = View.VISIBLE
        saveReviewButton.visibility = View.VISIBLE
    }

    private fun enableRadioGroupChildren(radioGroup: RadioGroup, enabled: Boolean) {
        for (i in 0 until radioGroup.childCount) {
            radioGroup.getChildAt(i).isEnabled = enabled
        }
    }

    private fun getPriceRadioButtonId(price: Int): Int {
        return when (price) {
            1 -> R.id.price1
            2 -> R.id.price2
            3 -> R.id.price3
            4 -> R.id.price4
            5 -> R.id.price5
            else -> -1
        }
    }

    private fun getFlavorRadioButtonId(flavor: String): Int {
        return when (flavor) {
            "Strong" -> R.id.flavorStrong
            "Mild" -> R.id.flavorMild
            "Decaf" -> R.id.flavorDecaf
            else -> -1
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
                    reviewImageView.setImageURI(selectedImageUri)
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val bitmap = data?.extras?.get("data") as Bitmap
                    selectedImageUri = getImageUriFromBitmap(bitmap)
                    reviewImageView.setImageURI(selectedImageUri)
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

    private fun saveReview() {
        var updatedReview = review.copy(
            coffeeShop = coffeeShopNameAutoCompleteTextView.text.toString(),
            rate = ratingBar.rating.toInt(),
            price = when (priceRadioGroup.checkedRadioButtonId) {
                R.id.price1 -> 1
                R.id.price2 -> 2
                R.id.price3 -> 3
                R.id.price4 -> 4
                R.id.price5 -> 5
                else -> 0
            },
            flavor = when (flavorRadioGroup.checkedRadioButtonId) {
                R.id.flavorStrong -> "Strong"
                R.id.flavorMild -> "Mild"
                R.id.flavorDecaf -> "Decaf"
                else -> ""
            },
            body = reviewBodyEditText.text.toString()
        )

        selectedImageUri?.let { uri ->
            val storageRef = storage.reference.child("reviewImages/${review.id}.jpg")
            storageRef.putFile(uri)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        updatedReview = updatedReview.copy(photo = downloadUri.toString())
                        saveUpdatedReview(updatedReview)
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error uploading image", Toast.LENGTH_SHORT).show()
                }
        } ?: saveUpdatedReview(updatedReview)
    }

    private fun saveUpdatedReview(updatedReview: Review) {
        firestore.collection("reviews").document(updatedReview.id).set(updatedReview)
            .addOnSuccessListener {
                Toast.makeText(this, "Review updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error updating review", Toast.LENGTH_SHORT).show()
            }
    }
}
