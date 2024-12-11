package com.example.freshguard.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.freshguard.MainActivity
import com.example.freshguard.R
import com.example.freshguard.data.local.UserPreferences
import com.example.freshguard.data.repository.AuthRepository
import com.example.freshguard.data.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    private val userPreferences: UserPreferences by lazy { UserPreferences(applicationContext) }
    private val authRepository: AuthRepository by lazy { AuthRepository(ApiConfig.apiAuthService, userPreferences) }

    private val authViewModel: AuthViewModel by lazy {
        ViewModelProvider(this, AuthViewModelFactory(application, authRepository))[AuthViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        // Inisialisasi ApiConfig setelah registrasi berhasil
        ApiConfig.init(applicationContext) // Panggil init di sini

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
                        CoroutineScope(Dispatchers.IO).launch {
                            userPreferences.saveToken(token)
                        }
                    }

                    Toast.makeText(this, "Success: ${response?.message}", Toast.LENGTH_SHORT).show()
                    // Arahkan ke MainActivity setelah registrasi sukses
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                },
                onError = { errorMessage ->
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }
}
