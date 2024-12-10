package com.example.freshguard.data.repository

import com.example.freshguard.data.local.UserPreference
import com.example.freshguard.data.request.LoginRequest
import com.example.freshguard.data.request.RegisRequest
import com.example.freshguard.data.response.HistoryResponse
import com.example.freshguard.data.response.LoginResponse
import com.example.freshguard.data.response.RegisResponse
import com.example.freshguard.data.retrofit.ApiAuth
import com.example.freshguard.data.retrofit.ApiService
import retrofit2.Response

class FreshRepository private constructor(
    private val apiService: ApiService,
    private val pref: UserPreference
){
    fun getToken() = pref.getToken()

    suspend fun saveToken(token: String) = pref.saveToken(token)

    suspend fun login(email: String, password: String): LoginResponse {
        val loginRequest = LoginRequest(email, password)
        return apiService.login(loginRequest)
    }

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
        val response = apiService.register(requestBody)
        if (response.isSuccessful) {
            response.body()?.data?.token?.let { token ->
                pref.saveToken(token)
            }
        }

        return response
    }


    companion object {
        @Volatile
        private var instance: FreshRepository? = null

        fun getInstance(apiService: ApiService, pref: UserPreference): FreshRepository =
            instance ?: synchronized(this) {
                instance ?: FreshRepository(apiService, pref).also { instance = it }
            }
    }

}