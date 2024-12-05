package com.example.freshguard.ui.scan

import android.graphics.BitmapFactory
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.freshguard.R
import com.example.freshguard.databinding.ActivityScanResultBinding
import android.util.Base64

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
        val imageBase64 = intent.getStringExtra("IMAGE_BASE64") // Ambil imageBase64


        binding.result1.text = score ?: "tidak diketahui"
        binding.result2.text = produce ?: "tidak diketahui"
        binding.tvResultText.text = if (isConsumable) "Produk ini layak dikonsumsi" else "Produk ini tidak layak dikonsumsi"
        // Menampilkan gambar jika ada
        imageBase64?.let {
            val decodedString = Base64.decode(it, Base64.DEFAULT)
            val decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.size)
            binding.imgScanResult.setImageBitmap(decodedByte) // Menampilkan gambar
        }
    }
}