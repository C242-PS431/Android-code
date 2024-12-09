package com.example.freshguard.ui.scan

import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
        val isConsumable = intent.getBooleanExtra("IS_CONSUMABLE", false)
        val imageUriString = intent.getStringExtra("IMAGE_URI") // Ambil IMAGE_URI yang dikirimkan dari ValidationFragment

        binding.result1.text = score ?: "tidak diketahui"
        binding.result2.text = produce ?: "tidak diketahui"
        binding.tvResultText.text = if (isConsumable) "Produk ini layak dikonsumsi" else "Produk ini tidak layak dikonsumsi"

        // Menampilkan gambar jika ada
        imageUriString?.let {
            val imageUri = Uri.parse(it) // Mengonversi string menjadi Uri
            binding.imgScanResult.setImageURI(imageUri) // Menampilkan gambar dari URI
        }
    }
}
