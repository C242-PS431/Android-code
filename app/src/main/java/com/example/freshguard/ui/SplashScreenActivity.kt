package com.example.freshguard.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.freshguard.MainActivity
import com.example.freshguard.R
import com.example.freshguard.data.local.UserPreferences
import com.example.freshguard.ui.register.LoginActivity
import com.example.freshguard.ui.register.RegisterActivity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var userPreferences: UserPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        // Inisialisasi UserPreferences
        userPreferences = UserPreferences(applicationContext)

        // Jalankan pemeriksaan token
        lifecycleScope.launch {
            delay(2000) // Delay selama 2 detik (untuk tampilan splash screen)

            // Cek apakah token tersedia
            val token = userPreferences.getToken()
            if (!token.isNullOrEmpty()) {
                // Jika token ada, arahkan ke MainActivity (yang memiliki ScanFragment)
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
            } else {
                // Jika token tidak ada, arahkan ke RegisterActivity
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
            }
            finish() // Selesaikan SplashScreenActivity agar tidak bisa kembali
        }
    }
}