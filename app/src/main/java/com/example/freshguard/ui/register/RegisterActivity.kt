package com.example.freshguard.ui.register

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.freshguard.R
import com.example.freshguard.data.local.UserPreferences
import com.example.freshguard.data.repository.AuthRepository
import com.example.freshguard.data.retrofit.ApiAuth
import com.example.freshguard.data.retrofit.ApiConfig
import com.example.freshguard.data.retrofit.ApiConfig.apiAuthService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {
    private val userPreferences = UserPreferences(applicationContext)
    private val authRepository = AuthRepository(ApiConfig.apiAuthService, userPreferences)
    // Menggunakan ViewModel dengan AuthViewModelFactory untuk menyediakan AuthViewModel
    private val authViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(application, authRepository) // Menyediakan Application dan AuthRepository
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        val edtName = findViewById<EditText>(R.id.edtName)
        val edtUsername = findViewById<EditText>(R.id.edtUsername)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)

        btnRegister.setOnClickListener {
            val name = edtName.text.toString().trim()
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.registerUser(
                name = name,
                username = username,
                password = password,
                onSuccess = { response ->
                    response?.data?.token?.let { token ->
//                        val userPreferences = UserPreferences(applicationContext)
                        CoroutineScope(Dispatchers.IO).launch {
                            userPreferences.saveAuthToken(token) // Memanggil saveAuthToken di dalam coroutine
                        }// Menyimpan token ke DataStore
                    }
                    Toast.makeText(this, "Success: ${response?.message}", Toast.LENGTH_SHORT).show()
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }

    }
}