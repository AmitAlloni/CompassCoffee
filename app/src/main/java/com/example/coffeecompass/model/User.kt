package com.example.coffeecompass.model


data class User(
    val uid: String = "",
    val userName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val followers: ArrayList<String> = arrayListOf(),
    val following: ArrayList<String> = arrayListOf(),
    val reviews: ArrayList<String> = arrayListOf(),
    val userSettings: UserSettings = UserSettings()
)

data class UserSettings(
    val flavor: String = "Strong",
    val price: String = "$$",
    val location: String = "My location",
    val distance: Float = 3.0f,
    val messages: Boolean = true,
    val emailNotifications: Boolean = true,
    val pushNotifications: Boolean = true
)
