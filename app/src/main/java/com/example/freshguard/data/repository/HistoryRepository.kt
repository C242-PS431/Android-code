package com.example.freshguard.data.repository

import com.example.freshguard.data.response.HistoryResponse
import com.example.freshguard.data.retrofit.ApiHistory
import retrofit2.Response

class HistoryRepository(private val apiHistory: ApiHistory) {
    suspend fun getHistoryScan(
        page: Int,
        perPage: Int
    ): Response<HistoryResponse> {
        return try {
            val response = apiHistory.getScanHistory(page, perPage)
            response
        } catch (e: Exception) {
            throw e
        }
    }
}