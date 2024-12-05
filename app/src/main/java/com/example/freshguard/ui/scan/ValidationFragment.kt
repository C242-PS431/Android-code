package com.example.freshguard.ui.scan

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.freshguard.R
import com.example.freshguard.data.repository.ScanRepository
import com.example.freshguard.data.request.ScanRequestBody
import com.example.freshguard.data.response.ScanResponse
import com.example.freshguard.data.response.ScanResult
import com.example.freshguard.data.retrofit.ApiConfig
import com.example.freshguard.databinding.FragmentValidationBinding

class ValidationFragment : Fragment() {
    private lateinit var binding: FragmentValidationBinding
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var viewModel: ScanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentValidationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentValidationBinding.bind(view)

        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel = ViewModelProvider(this, ScanViewModelFactory(ScanRepository(ApiConfig.apiScanService))).get(ScanViewModel::class.java)

        viewModel.scanResult.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                navigateToResultPage(response)
            } else {
                Toast.makeText(requireContext(), "Gagal mendapatkan hasil, coba lagi!", Toast.LENGTH_SHORT).show()
            }
        }
        
        binding.buttonResult.setOnClickListener {

            val smell = when (binding.radioGroupBau.checkedRadioButtonId) {
                R.id.radioSegar -> "Segar"
                R.id.radioBusuk -> "Busuk"
                R.id.radioNetral -> "Netral"
                else -> null
            }

            val texture = when (binding.radioGroupTekstur.checkedRadioButtonId) {
                R.id.radioKeras -> "Keras"
                R.id.radioLembek -> "Lembek"
                R.id.radioNormal -> "Normal"
                else -> null
            }
            val verifiedStore = when (binding.radioGroupStore.checkedRadioButtonId) {
                R.id.radioBenar -> "Benar"
                R.id.radioSalah -> "Salah"
                else -> null
            }
            if (smell != null && texture != null && verifiedStore != null && sharedViewModel.imageBase64 != null) {
                // Konversi smell dan texture ke bahasa Inggris
                val (smellEnglish, textureEnglish) = translateToEnglish(smell, texture)
                val verifiedStoreEnglish = if (verifiedStore == "Benar") "True" else "False"

                // Buat request body
                val requestBody = ScanRequestBody(
                    image = sharedViewModel.imageBase64!!,
                    smell = smellEnglish,
                    texture = textureEnglish,
                    verifiedStore = verifiedStoreEnglish
                )
                // Kirim ke API
                viewModel.postScanFreshness("Bearer [31|IwQLhpHEiQpCiKXKQBZcZYzBgaN6wAYxuT3Rlfrve05a75a3]", requestBody)
                Toast.makeText(requireContext(), "Data berhasil dikirim", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(requireContext(), "mohon Lengkapi semua data dulu yaa", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToResultPage(response: ScanResponse) {
        // Ambil scanResult dari response
        val scanResult = response.data?.scanResult
        scanResult?.let {
            val intent = Intent(requireContext(), ScanResultActivity::class.java).apply {
                putExtra("SCORE", it.freshnessScore)
                putExtra("PRODUCE", it.produce)
                putExtra("IS_CONSUMABLE", it.isConsumable()) // Menentukan apakah produk layak konsumsi
                putExtra("IMAGE_BASE64", sharedViewModel.imageBase64)
            }
            startActivity(intent)
        }
    }

    private fun translateToEnglish(smell: String, texture: String): Pair<String, String>  {
        val smellMap = mapOf(
            "Segar" to "Fresh",
            "Busuk" to "Rotten",
            "Netral" to "Neutral"
        )

        val textureMap = mapOf(
            "Keras" to "Hard",
            "Lembek" to "Soft",
            "Normal" to "Normal"
        )

        return Pair(
            smellMap[smell] ?: "Unknown",
            textureMap[texture] ?: "Unknown"
        )
    }
}