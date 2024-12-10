package com.example.freshguard.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshguard.data.local.UserPreference
import com.example.freshguard.data.repository.AuthRepository
import com.example.freshguard.data.repository.FreshRepository
import com.example.freshguard.data.response.LoginResponse
import com.example.freshguard.data.response.RegisResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(private val freshRepository: FreshRepository): ViewModel() {
    fun registerUser(
        name: String,
        username: String,
        password: String,
        onSuccess: (RegisResponse?) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response: Response<RegisResponse> = freshRepository.registerUser(name, username, password)
                if (response.isSuccessful) {
                    val token = response.body()?.data?.token
                    token?.let {
                        freshRepository.saveToken(token)
                    }
                    onSuccess(response.body())
                } else {
                    onError("Registration failed: ${response.message()}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }

    fun login(
        username: String,
        password: String,
        onSuccess: (LoginResponse?) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response = freshRepository.login(username, password)
                if (response.data.token.isNotEmpty()) {
                    freshRepository.saveToken(response.data.token)
                    onSuccess(response)
                } else {
                    onError("Registration failed: ${response.message}")
                }
            } catch (e: Exception) {
                onError("Error: ${e.message}")
            }
        }
    }
}