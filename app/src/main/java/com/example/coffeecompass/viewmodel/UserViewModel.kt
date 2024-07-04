package com.example.coffeecompass.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coffeecompass.model.User
import com.example.coffeecompass.repository.UserRepository
import kotlinx.coroutines.launch

class UserViewModel : ViewModel() {

    private val userRepository = UserRepository()
    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    fun getUserById(userId: String) {
        viewModelScope.launch {
            val user = userRepository.getUserById(userId)
            _user.postValue(user)
        }
    }

    fun toggleLikeCoffeeShop(coffeeShopId: String) {
        _user.value?.let { user ->
            viewModelScope.launch {
                userRepository.toggleLikeCoffeeShop(user.uid, coffeeShopId)
                _user.postValue(userRepository.getUserById(user.uid)) // Refresh user data
            }
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            userRepository.updateUser(user)
            _user.postValue(user)
        }
    }
}
