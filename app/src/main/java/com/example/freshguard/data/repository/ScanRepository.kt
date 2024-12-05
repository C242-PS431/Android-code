package com.example.freshguard.data.repository

import com.example.freshguard.data.request.ScanRequestBody
import com.example.freshguard.data.response.ScanResponse
import com.example.freshguard.data.retrofit.ApiScan
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response

class ScanRepository(private val apiScan: ApiScan) {
    suspend fun postScanFreshness(
        token: String,
        scanRequestBody: ScanRequestBody
    ): Response<ScanResponse> {
        return apiScan.postScanFreshness(token, scanRequestBody)
    }
}