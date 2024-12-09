package com.example.capstone

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.capstone.retrofit.ApiService
import com.example.capstone.response.LoginResponse
import com.example.capstone.register.Result
import com.example.capstone.request.LoginRequest
import com.example.capstone.request.RegisterRequest
import com.example.capstone.response.RegisterResponse
import kotlinx.coroutines.Dispatchers
import retrofit2.Response

class UserViewModel(private val apiService: ApiService) : ViewModel() {

    fun registerUser(registerRequest: RegisterRequest) = liveData(Dispatchers.IO) {
        try {
            val response: Response<RegisterResponse> = apiService.register(registerRequest)
            if (response.isSuccessful) {
                val registerResponse = response.body()
                emit(Result.Success(registerResponse))  // Emit success
            } else {
                emit(Result.Error(Exception("Error: ${response.code()}"))) // Emit error
            }
        } catch (e: Exception) {
            emit(Result.Error(e))
        }
    }

    // Function to login a user
    fun loginUser(loginRequest: LoginRequest) = liveData(Dispatchers.IO) {
        try {
            val response: Response<LoginResponse> = apiService.login(loginRequest)
            if (response.isSuccessful) {
                val loginResponse = response.body()
                emit(Result.Success(loginResponse))  // Emit success
            } else {
                emit(Result.Error(Exception("Error: ${response.code()}"))) // Emit error
            }
        } catch (e: Exception) {
            emit(Result.Error(e)) // Emit error
        }
    }
}
