package com.example.freshguard.data.retrofit

import com.example.freshguard.data.request.LoginRequest
import com.example.freshguard.data.request.RegisRequest
import com.example.freshguard.data.response.HistoryResponse
import com.example.freshguard.data.response.LoginResponse
import com.example.freshguard.data.response.RegisResponse
import com.example.freshguard.data.response.ScanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {
    @POST("/api/auth/register")
    suspend fun register(
        @Body requestBody: RegisRequest
    ): Response<RegisResponse>


    @POST("api/auth/login")
    suspend fun login(
        @Body loginRequest: LoginRequest
    ): LoginResponse

}