package com.example.freshguard.data.repository

import com.example.freshguard.data.local.UserPreferences
import com.example.freshguard.data.request.RegisRequest
import com.example.freshguard.data.response.RegisResponse
import com.example.freshguard.data.retrofit.ApiAuth
import retrofit2.Response

class AuthRepository(private val apiAuth: ApiAuth, private val userPreferences: UserPreferences) {
    suspend fun registerUser(
        name: String,
        username: String,
        password: String
    ): Response<RegisResponse> {
        val requestBody = RegisRequest(
            name = name,
            username = username,
            password = password
        )
        val response = apiAuth.register(requestBody)
        if (response.isSuccessful) {
            response.body()?.data?.token?.let { token ->
                // Simpan token ke DataStore setelah berhasil registrasi
                userPreferences.saveAuthToken(token)
            }
        }

        return response
    }
}