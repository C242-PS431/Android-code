package com.example.freshguard.data.retrofit

import com.example.freshguard.data.request.RegisRequest
import com.example.freshguard.data.response.RegisResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiAuth {
    @POST("/api/auth/register")
    suspend fun register(
        @Body requestBody: RegisRequest
    ): Response<RegisResponse>
}