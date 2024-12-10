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
import com.example.freshguard.data.local.UserPreference
import com.example.freshguard.data.local.dataStore
import com.example.freshguard.data.repository.FreshRepository
import com.example.freshguard.ui.register.RegisterActivity
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.freshguard.ui.register.LoginActivity

class SplashScreenActivity : AppCompatActivity() {

    private lateinit var userPreferences: UserPreference
    private lateinit var freshRepository: FreshRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        // Jalankan pemeriksaan token
        lifecycleScope.launch {
            delay(2000) // Delay selama 2 detik (untuk tampilan splash screen)

            // Menggunakan dataStore sebagai extension property global
            val userPreference = UserPreference.getInstance(this@SplashScreenActivity.dataStore)
            val token = userPreference.getToken().firstOrNull()

            if (token != null) {
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
