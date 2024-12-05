package com.example.freshguard.data.retrofit

import com.example.freshguard.data.request.ScanRequestBody
import com.example.freshguard.data.response.ScanResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiScan {
    @POST("api/v1/scans/freshness")
    suspend fun postScanFreshness(
        @Header("Authorization") token: String,
        @Body requestBody: ScanRequestBody
    ): Response<ScanResponse>
}