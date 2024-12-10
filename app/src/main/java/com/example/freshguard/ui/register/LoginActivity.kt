package com.example.freshguard.ui.register

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.freshguard.MainActivity
import com.example.freshguard.R
import com.example.freshguard.ui.ViewModelFactory

class LoginActivity : AppCompatActivity() {
    private val factory: ViewModelFactory = ViewModelFactory.getInstance(this)
    private val authViewModel: AuthViewModel by viewModels<AuthViewModel>{
        factory
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        val edtUsername = findViewById<EditText>(R.id.edtUsername)
        val edtPassword = findViewById<EditText>(R.id.edtPassword)
        val btnRegister = findViewById<Button>(R.id.btnRegister)
        val btnLogin = findViewById<Button>(R.id.btnLogin)

        btnLogin.setOnClickListener{
            val registerIntent = Intent(this, RegisterActivity::class.java)
            startActivity(registerIntent)
            finish()
        }
        btnRegister.setOnClickListener {
            val username = edtUsername.text.toString().trim()
            val password = edtPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            authViewModel.login(
                username = username,
                password = password,
                onSuccess = { response ->
                    Toast.makeText(this, "Success: ${response?.message}", Toast.LENGTH_SHORT).show()
                    // Arahkan ke MainActivity setelah registrasi sukses
                    val intent = Intent(this, MainActivity::class.java)
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