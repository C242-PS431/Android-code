package com.example.freshguard.data.retrofit

import android.content.Context
import com.example.freshguard.data.local.UserPreference
import com.example.freshguard.data.local.dataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor

object ApiCon {
    // Base URL API
    private const val BASE_URL = "http://35.219.27.170/"

    // Buat instance Retrofit
    private fun getRetrofit(context: Context): Retrofit {
        // Logging untuk debug
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
        }

        // Interceptor untuk menambahkan Authorization ke setiap request
        val authInterceptor = Interceptor { chain ->
            val userPreference = UserPreference.getInstance(context.dataStore) // Buat instance UserPreference
            val token = runBlocking { userPreference.getToken().first() } // Ambil token dari DataStore
            val requestBuilder = chain.request().newBuilder()

            // Tambahkan header Authorization jika token tidak null/empty
            if (token != null) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }

        // OkHttpClient dengan interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Menambahkan interceptor untuk logging
            .addInterceptor(authInterceptor)    // Menambahkan interceptor untuk Authorization
            .connectTimeout(60, TimeUnit.SECONDS) // Timeout koneksi
            .readTimeout(60, TimeUnit.SECONDS)    // Timeout membaca data dari server
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    private lateinit var retrofit: Retrofit

    fun init (context: Context) {
        if (!::retrofit.isInitialized) {
            retrofit = getRetrofit(context)
        }
    }

    val apiScanService: ApiScan
        get() = retrofit.create(ApiScan::class.java)

    val apiHistoryService: ApiHistory
        get() = retrofit.create(ApiHistory::class.java)

    val apiAuthService: ApiAuth
        get() = retrofit.create(ApiAuth::class.java)


}