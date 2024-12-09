package com.example.capstone.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.capstone.retrofit.ApiService
import com.example.capstone.register.Result
import com.example.capstone.request.LoginRequest
import com.example.capstone.request.RegisterRequest
import com.example.capstone.response.LoginResponse
import com.example.capstone.response.RegisterResponse
import com.example.capstone.response.User  // Import model User

class AuthRepository(private val apiService: ApiService, private val context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    // Fungsi untuk login
    suspend fun login(username: String, password: String): Result<LoginResponse> {
        return try {
            val response = apiService.login(LoginRequest(username, password))
            if (response.isSuccessful) {
                response.body()?.let {
                    it.data?.user?.let { user ->
                        // Setelah login berhasil, simpan session
                        saveSession(user)  // Simpan data pengguna yang diperoleh dari response
                    }
                    Result.Success(it)  // Return success if the response is not null
                } ?: Result.Error(Exception("Response body is null"))  // Error if body is null
            } else {
                Result.Error(Exception("Login failed: ${response.message()}"))  // Handle API failure
            }
        } catch (e: Exception) {
            Result.Error(e)  // Catch any exception and return as an error
        }
    }

    // Fungsi untuk register
    suspend fun register(registerRequest: RegisterRequest): Result<RegisterResponse> {
        return try {
            val response = apiService.register(registerRequest)
            if (response.isSuccessful) {
                response.body()?.let {
                    Result.Success(it)  // Return success if the response is not null
                } ?: Result.Error(Exception("Response body is null"))  // Error if body is null
            } else {
                Result.Error(Exception("Registration failed: ${response.message()}"))  // Handle API failure
            }
        } catch (e: Exception) {
            Result.Error(e)  // Catch any exception and return as an error
        }
    }

    // Fungsi untuk menyimpan session pengguna (gunakan SharedPreferences)
    fun saveSession(user: User) {
        val editor = sharedPreferences.edit()
        editor.putString("user_id", user.id)
        editor.putString("username", user.username)
        editor.putString("name", user.name)
        // Simpan atribut lain jika perlu
        editor.apply()  // Simpan perubahan
    }

    // Fungsi untuk mengambil session pengguna (misalnya, saat aplikasi dimulai)
    fun getSession(): User? {
        val userId = sharedPreferences.getString("user_id", null)
        val username = sharedPreferences.getString("username", null)
        val name = sharedPreferences.getString("name", null)

        return if (userId != null && username != null && name != null) {
            User(id = userId, username = username, name = name)  // Menggunakan User untuk membuat objek pengguna
        } else {
            null  // Jika tidak ada session
        }
    }

    // Fungsi untuk menghapus session pengguna
    fun clearSession() {
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
}
