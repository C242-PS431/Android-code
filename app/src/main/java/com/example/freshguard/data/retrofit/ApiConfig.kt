package com.example.freshguard.data.retrofit

import android.content.Context
import com.example.freshguard.data.local.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiConfig {
    private const val BASE_URL = "http://35.219.27.170"
    // Tambahkan token statis sementara
//    private const val STATIC_TOKEN = "Bearer 36|gFMlrLjPfQA40yT6h5wGy4Ed5PITKGspXqbyLxP96dee61c2"

    private val client: OkHttpClient
        get() = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val originalRequest = chain.request()
                val userPreferences = UserPreferences(chain.request().tag(Context::class.java)!!)
                val token = runBlocking { userPreferences.authToken.firstOrNull() }

                val modifiedRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token") // Menggunakan token statis
                    .addHeader("Accept", "application/json")
                    .build()
                chain.proceed(modifiedRequest)
            }
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .build()
    private val retrofit: Retrofit
        get() = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    val apiScanService: ApiScan
        get() = retrofit.create(ApiScan::class.java)

    val apiHistoryService: ApiHistory
        get() = retrofit.create(ApiHistory::class.java)

    val apiAuthService: ApiAuth
        get() = retrofit.create(ApiAuth::class.java)
}
