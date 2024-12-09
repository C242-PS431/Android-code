package com.example.capstone.retrofit

import com.example.capstone.request.LoginRequest
import com.example.capstone.request.RegisterRequest
import com.example.capstone.response.LoginResponse
import com.example.capstone.response.RegisterResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response


interface ApiService {
    @POST("api/auth/register")
    suspend fun register(
        @Body registerRequest: RegisterRequest
    ): Response<RegisterResponse>

    @POST("/api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>

}

