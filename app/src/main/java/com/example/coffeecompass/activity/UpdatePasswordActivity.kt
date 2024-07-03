package com.example.coffeecompass.activity

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.coffeecompass.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth

class UpdatePasswordActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var oldPasswordEditText: EditText
    private lateinit var newPasswordEditText: EditText
    private lateinit var submitButton: Button
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)

        auth = FirebaseAuth.getInstance()

        oldPasswordEditText = findViewById(R.id.editTextOldPassword)
        newPasswordEditText = findViewById(R.id.editTextNewPassword)
        submitButton = findViewById(R.id.buttonSubmit)
        progressBar = findViewById(R.id.progressBar)

        submitButton.setOnClickListener {
            val oldPassword = oldPasswordEditText.text.toString().trim()
            val newPassword = newPasswordEditText.text.toString().trim()

            if (oldPassword.isNotEmpty() && newPassword.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                updatePassword(oldPassword, newPassword)
            } else {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updatePassword(oldPassword: String, newPassword: String) {
        val user = auth.currentUser
        if (user != null && user.email != null) {
            val credential = EmailAuthProvider.getCredential(user.email!!, oldPassword)

            user.reauthenticate(credential).addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    user.updatePassword(newPassword).addOnCompleteListener { updateTask ->
                        progressBar.visibility = View.GONE
                        if (updateTask.isSuccessful) {
                            Toast.makeText(this, "Password updated", Toast.LENGTH_SHORT).show()
                            finish()
                        } else {
                            Toast.makeText(this, "Error updating password", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Old password is incorrect", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
