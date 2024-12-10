package com.example.freshguard.data.retrofit

import com.example.freshguard.data.response.ScanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiScan {
    @Multipart
    @POST("api/v1/scans/freshness")
    suspend fun postScanFreshness(
        @Part image: MultipartBody.Part,
        @Part("smell") smell: RequestBody,
        @Part("texture") texture: RequestBody,
        @Part("verifiedStore") verifiedStore: RequestBody
    ): Response<ScanResponse>

}