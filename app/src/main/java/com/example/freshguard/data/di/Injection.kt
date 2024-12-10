package com.example.freshguard.data.di

import android.content.Context
import com.example.freshguard.data.local.UserPreference
import com.example.freshguard.data.local.dataStore
import com.example.freshguard.data.repository.FreshRepository
import com.example.freshguard.data.retrofit.ApiConfig

object Injection {
    fun provideRepository(context: Context): FreshRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService(pref)
        return FreshRepository.getInstance(apiService, pref)
    }
}