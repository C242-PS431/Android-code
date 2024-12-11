package com.example.freshguard.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.freshguard.MainActivity
import com.example.freshguard.R
import com.example.freshguard.data.local.UserPreferences
import com.example.freshguard.data.repository.AuthRepository
import com.example.freshguard.data.retrofit.ApiConfig


class LoginActivity : AppCompatActivity() {

    private lateinit var edtUsername: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnRegister: Button

    private val userPreferences: UserPreferences by lazy { UserPreferences(applicationContext) }
    private val authRepository by lazy { AuthRepository(ApiConfig.apiAuthService, userPreferences) } // Pastikan Anda menyediakan AuthRepository
    private val loginViewModel: AuthViewModel by viewModels {
        AuthViewModelFactory(application, authRepository) // Gunakan factory untuk membuat instance AuthViewModel
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        edtUsername = findViewById(R.id.edtUsername)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegister = findViewById(R.id.btnRegister)

        // Listener untuk tombol login
        btnLogin.setOnClickListener {
            val username = edtUsername.text.toString()
            val password = edtPassword.text.toString()

            if (username.isEmpty()) {
                showToast("Username tidak boleh kosong")
            } else if (password.isEmpty()) {
                showToast("Password tidak boleh kosong")
            } else {
                // Panggil fungsi login dari ViewModel
                loginViewModel.loginUser(username, password, onSuccess = {
                    // Berhasil login
                    showToast("Login berhasil!")
                    // Lakukan tindakan selanjutnya, seperti pindah ke halaman lain
                    startActivity(Intent(this, MainActivity::class.java))
                }, onError = {
                    // Gagal login
                    showToast(it)
                })
            }
        }

        btnRegister.setOnClickListener {
            // Navigate to RegisterActivity
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
