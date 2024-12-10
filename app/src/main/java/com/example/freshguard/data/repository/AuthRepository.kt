package com.example.freshguard.data.repository

import com.example.freshguard.data.local.UserPreference
import com.example.freshguard.data.request.RegisRequest
import com.example.freshguard.data.response.RegisResponse
import com.example.freshguard.data.retrofit.ApiAuth
import retrofit2.Response

class AuthRepository(private val apiAuth: ApiAuth, private val userPreferences: UserPreference) {

}