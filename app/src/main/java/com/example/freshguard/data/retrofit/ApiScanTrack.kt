package com.example.freshguard.data.retrofit

import com.example.freshguard.data.response.ScanTrackResponse
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiScanTrack {
    @POST("/api/v1/scans/{scanId}/track")
    suspend fun trackScan(
        @Path("scanId") scanId: String
    ): ScanTrackResponse
}