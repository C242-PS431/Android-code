package com.example.freshguard.ui.scan

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.freshguard.MainActivity
import com.example.freshguard.R
import com.example.freshguard.databinding.ActivityScanResultBinding

class ScanResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityScanResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityScanResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val score = intent.getStringExtra("SCORE")
        val produce = intent.getStringExtra("PRODUCE")

        // Menambahkan deskripsi pada hasil
        binding.result1.text = getString(R.string.freshness_score_label, score ?: getString(R.string.unknown))
        binding.result2.text = getString(R.string.produce_label, produce ?: getString(R.string.unknown))

        // Menangani klik tombol kembali ke halaman Scan
        binding.button.setOnClickListener {
            // Membuka ScanActivity dan menutup ScanResultActivity
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Menutup ScanResultActivity
        }
    }
}
