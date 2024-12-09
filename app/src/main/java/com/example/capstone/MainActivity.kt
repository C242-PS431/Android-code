package com.example.capstone

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.capstone.login.LoginActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Mengakses SharedPreferences
        sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)

        // Cek apakah pengguna sudah login
        val isLoggedIn = sharedPref.getBoolean("is_logged_in", false)

        if (isLoggedIn) {
            setContentView(R.layout.activity_main)
            initializeBottomNavigation()
        } else {
            redirectToLogin() // Arahkan ke LoginActivity jika belum login
        }
    }

    private fun redirectToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Close MainActivity supaya pengguna tidak bisa kembali ke halaman utama tanpa login
    }

    private fun initializeBottomNavigation() {
        bottomNavigation = findViewById(R.id.bottomNavigation)
        loadFragment(DashboardFragment())  // Mulai dengan fragment Dashboard

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(DashboardFragment())  // Tampilkan Dashboard fragment
                    true
                }
                // R.id.nav_scan -> {
                //    loadFragment(ScanFragment())  // Tampilkan Scan fragment
                //    true
                // }
                // R.id.nav_history -> {
                //    loadFragment(HistoryFragment())  // Tampilkan History fragment
                //    true
                // }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment())  // Tampilkan Profile fragment
                    true
                }
                else -> false
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .commit()
    }

    // Logout function
    fun logout() {
        // Hapus token dan status login dari SharedPreferences
        val editor = sharedPref.edit()
        editor.remove("auth_token")  // Hapus token
        editor.putBoolean("is_logged_in", false) // Set status login menjadi false
        editor.apply()

        // Arahkan pengguna ke LoginActivity
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()  // Tutup MainActivity agar pengguna tidak bisa kembali ke halaman utama tanpa login
    }
}
