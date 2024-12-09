package com.example.capstone.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.UserViewModel
import com.example.capstone.login.LoginActivity
import com.example.capstone.request.RegisterRequest

class RegisterActivity : AppCompatActivity() {

    // Menghubungkan RegisterViewModel
    private val registerUserViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_page)

        // Inisialisasi views
        val logoImageView: ImageView = findViewById(R.id.logoImageView)
        val nameEditText: EditText = findViewById(R.id.nameEditText)
        val usernameEditText: EditText = findViewById(R.id.signupUsernameEditText)
        val passwordEditText: EditText = findViewById(R.id.signupPasswordEditText)
        val signUpButton: Button = findViewById(R.id.signUpButton)
        val loginPrompt: TextView = findViewById(R.id.loginPrompt)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        // Apply animations
        applyAnimations(logoImageView, nameEditText, usernameEditText, passwordEditText, signUpButton)

        // Set click listener untuk loginPrompt untuk navigasi ke LoginActivity
        loginPrompt.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // Sign Up button functionality
        signUpButton.setOnClickListener {
            val name = nameEditText.text.toString()
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validasi dasar
            if (name.isNotEmpty() && username.isNotEmpty() && password.isNotEmpty()) {

                findViewById<ProgressBar>(R.id.progressBar).visibility = View.VISIBLE
                // Membuat objek RegisterRequest untuk dikirimkan ke API
                val registerRequest = RegisterRequest("name, username, password")

                // Panggil fungsi registerUser dari ViewModel
                registerUserViewModel.registerUser(registerRequest).observe(this) { result ->
                    when (result) {
                        is Result.Success -> {
                            // Jika registrasi sukses
                            Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show()

                            // Simpan token ke SharedPreferences
                            val token = result.data?.data?.token
                            saveToken("token")

                            // Navigasi ke MainActivity
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                        }
                        is Result.Error -> {
                            // Jika terjadi error
                            Toast.makeText(this, "Registration failed: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Fungsi untuk menyimpan token ke SharedPreferences
    private fun saveToken(token: String) {
        val sharedPref = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("auth_token", token) // Menyimpan token
        editor.apply()
    }

    private fun applyAnimations(
        logoImageView: ImageView,
        nameEditText: EditText,
        usernameEditText: EditText,
        passwordEditText: EditText,
        signUpButton: Button
    ) {
        // Animasi untuk logo (fade in)
        val fadeInLogo = AlphaAnimation(0f, 1f)
        fadeInLogo.duration = 1000
        logoImageView.startAnimation(fadeInLogo)

        // Animasi untuk Name EditText (fade in dengan delay)
        val fadeInName = AlphaAnimation(0f, 1f)
        fadeInName.duration = 1000
        fadeInName.startOffset = 500
        nameEditText.startAnimation(fadeInName)

        // Animasi untuk Username EditText (fade in dengan delay)
        val fadeInUsername = AlphaAnimation(0f, 1f)
        fadeInUsername.duration = 1000
        fadeInUsername.startOffset = 1000
        usernameEditText.startAnimation(fadeInUsername)

        // Animasi untuk Password EditText (fade in dengan delay)
        val fadeInPassword = AlphaAnimation(0f, 1f)
        fadeInPassword.duration = 1000
        fadeInPassword.startOffset = 1500
        passwordEditText.startAnimation(fadeInPassword)

        // Animasi untuk Sign Up Button (fade in dengan delay)
        val fadeInButton = AlphaAnimation(0f, 1f)
        fadeInButton.duration = 1000
        fadeInButton.startOffset = 2000
        signUpButton.startAnimation(fadeInButton)
    }
}
