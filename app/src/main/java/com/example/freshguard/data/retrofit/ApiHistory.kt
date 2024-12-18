package com.example.freshguard.data.retrofit

import com.example.freshguard.data.response.HistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiHistory {
    @GET("/api/v1/scans")
    suspend fun getScanHistory(
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 5
    ): Response<HistoryResponse>
}