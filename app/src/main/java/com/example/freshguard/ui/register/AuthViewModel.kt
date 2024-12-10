package com.example.freshguard.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshguard.data.local.UserPreferences
import com.example.freshguard.data.repository.AuthRepository
import com.example.freshguard.data.response.RegisResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class AuthViewModel(application: Application, private val authRepository: AuthRepository) : AndroidViewModel(application) {
    private val userPreferences: UserPreferences = UserPreferences(application.applicationContext)
    fun registerUser(
        name: String,
        username: String,
        password: String,
        onSuccess: (RegisResponse?) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val response: Response<RegisResponse> = authRepository.registerUser(name, username, password)
                if (response.isSuccessful) {
                    val token = response.body()?.data?.token
                    token?.let {
                        // Simpan token di UserPreferences setelah registrasi sukses
                        userPreferences.saveAuthToken(token) // Menggunakan token langsung tanpa it
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
}