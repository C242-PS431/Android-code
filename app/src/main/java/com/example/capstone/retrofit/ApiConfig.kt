package com.example.capstone.retrofit

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object ApiConfig {

    // Modify getApiService to accept a token parameter
    fun getApiService(token: String? = null): ApiService {
        val loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)

        // Add the Authorization header only if the token is provided
        val authInterceptor = Interceptor { chain ->
            val requestBuilder = chain.request().newBuilder()

            // Only add the Authorization header if token is not null
            token?.let {
                requestBuilder.addHeader("Authorization", "Bearer $it")
            }

            chain.proceed(requestBuilder.build())
        }

        // Build OkHttpClient with interceptors
        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(authInterceptor)  // Add auth interceptor here
            .build()

        // Create and return Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl("http://35.219.27.170")  // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(ApiService::class.java)
    }
}

