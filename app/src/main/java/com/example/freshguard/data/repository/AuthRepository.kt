package com.example.freshguard.data.repository

import com.example.freshguard.data.local.UserPreferences
import com.example.freshguard.data.request.LoginRequest
import com.example.freshguard.data.request.RegisRequest
import com.example.freshguard.data.response.LoginResponse
import com.example.freshguard.data.response.RegisResponse
import com.example.freshguard.data.retrofit.ApiAuth
import retrofit2.Response

class AuthRepository(private val apiAuth: ApiAuth, private val userPreferences: UserPreferences) {

    // Mengambil token dari SharedPreferences
    fun getToken(): String? {
        return userPreferences.getToken()
    }

    // Menyimpan token ke SharedPreferences
    fun saveToken(token: String) {
        userPreferences.saveToken(token)
    }

    // Fungsi login ke API
    suspend fun login(username: String, password: String): LoginResponse {
        val loginRequest = LoginRequest(username, password)
        val response = apiAuth.login(loginRequest)

        // Jika login berhasil, simpan token ke SharedPreferences
        response.data.token.let { token ->
            saveToken(token)
        }

        return response
    }

    suspend fun registerUser(
        name: String,
        username: String,
        password: String
    ): Result<RegisResponse> {
        return try {
            val requestBody = RegisRequest(name, username, password)
            val response = apiAuth.register(requestBody)
            if (response.isSuccessful && response.body() != null) {
                response.body()?.data?.token?.let { token ->
                    userPreferences.saveToken(token)
                }
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Registration failed with message: ${response.message()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
