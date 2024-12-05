package com.example.freshguard.data.retrofit

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val BASE_URL = "http://35.219.27.170"
    // Tambahkan token statis sementara
    private const val STATIC_TOKEN = "Bearer 31|IwQLhpHEiQpCiKXKQBZcZYzBgaN6wAYxuT3Rlfrve05a75a3"

    private val client: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val modifiedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", STATIC_TOKEN) // Menggunakan token statis
                    .build()
                chain.proceed(modifiedRequest)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    
    val apiScanService: ApiScan
        get() = retrofit.create(ApiScan::class.java)
}