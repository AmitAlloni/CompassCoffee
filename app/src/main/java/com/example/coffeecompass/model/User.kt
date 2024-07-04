package com.example.coffeecompass.model


data class User(
    val uid: String = "",
    val userName: String = "",
    val email: String = "",
    val profileImageUrl: String = "",
    val followers: ArrayList<String> = arrayListOf(),
    val following: ArrayList<String> = arrayListOf(),
    val reviews: ArrayList<String> = arrayListOf(),
    var likedCoffeeShops: ArrayList<String> = ArrayList(),
    val userSettings: UserSettings = UserSettings()
)

data class UserSettings(
    val flavor: String = "",
    val price: Int = 0,

)
