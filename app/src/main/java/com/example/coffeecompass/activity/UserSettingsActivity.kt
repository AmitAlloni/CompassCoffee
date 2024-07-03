package com.example.coffeecompass.activity

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeecompass.R
import com.example.coffeecompass.model.UserSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream

class UserSettingsActivity : AppCompatActivity() {

    private lateinit var firestore: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth

    private lateinit var flavorRadioGroup: RadioGroup
    private lateinit var priceRadioGroup: RadioGroup
    private lateinit var buttonUpdatePicture: Button
    private lateinit var buttonUpdatePassword: Button
    private lateinit var buttonLogout: Button
    private lateinit var buttonSavePreferences: Button
    private lateinit var progressBar: ProgressBar

    private var selectedImageUri: Uri? = null

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val REQUEST_IMAGE_CAPTURE = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)

        firestore = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()
        auth = FirebaseAuth.getInstance()

        flavorRadioGroup = findViewById(R.id.flavorRadioGroup)
        priceRadioGroup = findViewById(R.id.priceRadioGroup)
        buttonUpdatePicture = findViewById(R.id.buttonUpdatePicture)
        buttonUpdatePassword = findViewById(R.id.buttonUpdatePassword)
        buttonLogout = findViewById(R.id.buttonLogout)
        buttonSavePreferences = findViewById(R.id.buttonSavePreferences)
        progressBar = findViewById(R.id.progressBar)

        buttonUpdatePicture.setOnClickListener {
            showImagePickerOptions()
        }

        buttonUpdatePassword.setOnClickListener {
            startActivity(Intent(this, UpdatePasswordActivity::class.java))
        }

        buttonLogout.setOnClickListener {
            logout()
        }

        flavorRadioGroup.setOnCheckedChangeListener { _, _ ->
            buttonSavePreferences.visibility = View.VISIBLE
        }

        priceRadioGroup.setOnCheckedChangeListener { _, _ ->
            buttonSavePreferences.visibility = View.VISIBLE
        }

        buttonSavePreferences.setOnClickListener {
            savePreferences()
        }

        loadUserSettings()
    }

    private fun loadUserSettings() {
        val uid = auth.currentUser?.uid ?: return
        firestore.collection("users").document(uid).get()
            .addOnSuccessListener { document ->
                val userSettings = document.toObject(UserSettings::class.java)
                userSettings?.let { settings ->
                    setFlavorPreference(settings.flavor)
                    setPricePreference(settings.price)
                }
            }
    }

    private fun setFlavorPreference(flavor: String) {
        when (flavor) {
            "Strong" -> flavorRadioGroup.check(R.id.flavorStrong)
            "Mild" -> flavorRadioGroup.check(R.id.flavorMild)
            "Decaf" -> flavorRadioGroup.check(R.id.flavorDecaf)
        }
    }

    private fun setPricePreference(price: Int) {
        when (price) {
            1 -> priceRadioGroup.check(R.id.price1)
            2 -> priceRadioGroup.check(R.id.price2)
            3 -> priceRadioGroup.check(R.id.price3)
            4 -> priceRadioGroup.check(R.id.price4)
            5 -> priceRadioGroup.check(R.id.price5)
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
                    uploadProfilePicture(selectedImageUri)
                }
                REQUEST_IMAGE_CAPTURE -> {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    selectedImageUri = getImageUriFromBitmap(imageBitmap)
                    uploadProfilePicture(selectedImageUri)
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

    private fun uploadProfilePicture(uri: Uri?) {
        uri?.let {
            progressBar.visibility = View.VISIBLE
            val uid = auth.currentUser?.uid ?: return
            val storageRef = storage.reference.child("profileImages/$uid.jpg")
            storageRef.putFile(it)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                        firestore.collection("users").document(uid)
                            .update("profileImageUrl", downloadUri.toString())
                            .addOnSuccessListener {
                                progressBar.visibility = View.GONE
                                Toast.makeText(this, "Profile picture updated", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                progressBar.visibility = View.GONE
                                Toast.makeText(this, "Failed to update profile picture", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Failed to upload profile picture", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun savePreferences() {
        val uid = auth.currentUser?.uid ?: return
        val flavor = when (flavorRadioGroup.checkedRadioButtonId) {
            R.id.flavorStrong -> "Strong"
            R.id.flavorMild -> "Mild"
            R.id.flavorDecaf -> "Decaf"
            else -> ""
        }
        val price = when (priceRadioGroup.checkedRadioButtonId) {
            R.id.price1 -> 1
            R.id.price2 -> 2
            R.id.price3 -> 3
            R.id.price4 -> 4
            R.id.price5 -> 5
            else -> 0
        }
        val userSettings = UserSettings(flavor = flavor, price = price)
        firestore.collection("users").document(uid)
            .update(mapOf("userSettings" to userSettings))
            .addOnSuccessListener {
                Toast.makeText(this, "Preferences saved", Toast.LENGTH_SHORT).show()
                buttonSavePreferences.visibility = View.GONE
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to save preferences", Toast.LENGTH_SHORT).show()
            }
    }

    private fun logout() {
        auth.signOut()
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        finish()
    }
}
