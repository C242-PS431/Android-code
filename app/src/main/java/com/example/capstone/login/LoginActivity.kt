package com.example.capstone.login

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.EditText
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import android.view.View
import android.view.animation.AlphaAnimation
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.capstone.MainActivity
import com.example.capstone.R
import com.example.capstone.UserViewModel
import com.example.capstone.register.RegisterActivity
import com.example.capstone.register.Result
import com.example.capstone.request.LoginRequest


class LoginActivity : AppCompatActivity() {

    private val loginUserViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_page)

        val logoImageView: ImageView = findViewById(R.id.logoImageView)
        val usernameEditText: EditText = findViewById(R.id.usernameEditText)
        val passwordEditText: EditText = findViewById(R.id.passwordEditText)
        val loginButton: Button = findViewById(R.id.loginButton)
        val signupPrompt: TextView = findViewById(R.id.signupPrompt)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)

        // Apply animations on views
        applyAnimations(logoImageView, usernameEditText, passwordEditText, loginButton)

        // Set click listener for signupPrompt to navigate to RegisterActivity
        signupPrompt.setOnClickListener {

            progressBar.visibility = View.VISIBLE

            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Login button functionality
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Basic validation (can be expanded further)
            if (username.isNotEmpty() && password.isNotEmpty()) {
                val loginRequest = LoginRequest(username, password)

                // Observing login result
                loginUserViewModel.loginUser(loginRequest).observe(this, Observer { result ->
                    when (result) {
                        is Result.Success -> {
                            // Handle successful login
                            Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()

                            // Safely access the token
                            val token = result.data?.data?.token

                            // Check if token is not null
                            if (token != null) {
                                // Save token to SharedPreferences
                                val sharedPreferences: SharedPreferences = getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putString("TOKEN_KEY", token) // Save token with the key "TOKEN_KEY"
                                editor.apply()

                                // Proceed to MainActivity
                                val intent = Intent(this, MainActivity::class.java)
                                startActivity(intent)
                                finish() // Close LoginActivity
                            } else {
                                // Handle the case where token is null
                                Toast.makeText(this, "Token not found in response", Toast.LENGTH_SHORT).show()
                            }
                        }
                        is Result.Error -> {
                            // Handle login error
                            Toast.makeText(this, "Login Failed: ${result.exception.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
            } else {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun applyAnimations(logoImageView: ImageView, usernameEditText: EditText, passwordEditText: EditText, loginButton: Button) {
        // Logo animation (fade in)
        val fadeIn = AlphaAnimation(0f, 1f)
        fadeIn.duration = 1000 // Duration in milliseconds
        logoImageView.startAnimation(fadeIn)

        // Username EditText animation (fade in with delay)
        val fadeInUsername = AlphaAnimation(0f, 1f)
        fadeInUsername.duration = 1000
        fadeInUsername.startOffset = 500 // Delay animation by 500ms
        usernameEditText.startAnimation(fadeInUsername)

        // Password EditText animation (fade in with delay)
        val fadeInPassword = AlphaAnimation(0f, 1f)
        fadeInPassword.duration = 1000
        fadeInPassword.startOffset = 1000 // Delay animation by 1000ms
        passwordEditText.startAnimation(fadeInPassword)

        // Login Button animation (fade in with delay)
        val fadeInButton = AlphaAnimation(0f, 1f)
        fadeInButton.duration = 1000
        fadeInButton.startOffset = 1500 // Delay animation by 1500ms
        loginButton.startAnimation(fadeInButton)
    }
}
