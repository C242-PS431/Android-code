package com.example.freshguard.data.retrofit

import android.content.Context
import com.example.freshguard.data.local.UserPreferences
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import okhttp3.logging.HttpLoggingInterceptor


object ApiConfig {
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
            val token = UserPreferences(context).getToken() // Ambil token dari SharedPreferences
            val requestBuilder = chain.request().newBuilder()

            // Tambahkan header Authorization jika token tidak null/empty
            if (!token.isNullOrEmpty()) {
                requestBuilder.addHeader("Authorization", "Bearer $token")
            }

            chain.proceed(requestBuilder.build())
        }

        // OkHttpClient dengan interceptor
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor) // Menambahkan interceptor untuk logging
            .addInterceptor(authInterceptor)    // Menambahkan interceptor untuk Authorization
            .connectTimeout(30, TimeUnit.SECONDS) // Timeout koneksi
            .readTimeout(30, TimeUnit.SECONDS)    // Timeout membaca data dari server
            .build()

        // Membuat instance Retrofit
        return Retrofit.Builder()
            .baseUrl(BASE_URL)  // Base URL API
            .addConverterFactory(GsonConverterFactory.create())  // Converter untuk Gson
            .client(client)  // Menggunakan client dengan interceptor
            .build()
    }

    // Inisialisasi retrofit agar dapat digunakan di seluruh service
    private lateinit var retrofit: Retrofit

    // Fungsi init untuk inisialisasi retrofit hanya sekali
    fun init (context: Context) {
        if (!::retrofit.isInitialized) {
            retrofit = getRetrofit(context)
        }
    }

    // API Service untuk Scan
    val apiScanService: ApiScan
        get() = retrofit.create(ApiScan::class.java)

    // API Service untuk History
    val apiHistoryService: ApiHistory
        get() = retrofit.create(ApiHistory::class.java)

    // API Service untuk Authentication
    val apiAuthService: ApiAuth
        get() = retrofit.create(ApiAuth::class.java)
}