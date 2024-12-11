package com.example.freshguard.ui.register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshguard.data.local.UserPreferences
import com.example.freshguard.data.repository.AuthRepository
import com.example.freshguard.data.response.LoginResponse
import com.example.freshguard.data.response.RegisResponse
import kotlinx.coroutines.launch

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
            // Memanggil fungsi registerUser dari repository
            val result = authRepository.registerUser(name, username, password)

            // Menangani hasil dari Result
            result.onSuccess { response ->
                response.data?.token?.let { token ->
                    // Simpan token di UserPreferences setelah registrasi sukses
                    userPreferences.saveToken(token)
                }
                onSuccess(response) // Panggil callback onSuccess dan kirim response
            }

            result.onFailure { exception ->
                onError("Registration failed: ${exception.message}")
            }
        }
    }
    fun loginUser(
        username: String,
        password: String,
        onSuccess: (LoginResponse) -> Unit,
        onError: (String) -> Unit
    ) {
        viewModelScope.launch {
            try {
                // Panggil fungsi login dari repository
                val response = authRepository.login(username, password)

                // Periksa apakah login berhasil
                val token = response.data.token
                userPreferences.saveToken(token)

                onSuccess(response) // Kirim respons sukses ke UI

            } catch (e: Exception) {
                onError("Login gagal: ${e.message}") // Kirim pesan error ke UI
            }
        }
    }
}
