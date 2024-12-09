package com.example.freshguard.ui.scan

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.freshguard.R
import com.example.freshguard.data.repository.ScanRepository
import com.example.freshguard.data.response.ScanResponse
import com.example.freshguard.data.retrofit.ApiConfig
import com.example.freshguard.databinding.FragmentValidationBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

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
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        viewModel = ViewModelProvider(
            this,
            ScanViewModelFactory(requireContext(), ScanRepository(ApiConfig.apiScanService, requireContext()))
        ).get(ScanViewModel::class.java)

        viewModel.scanResult.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                navigateToResultPage(response)
            } else {
                Toast.makeText(requireContext(), "Gagal mendapatkan hasil: ${viewModel.errorMessage}", Toast.LENGTH_SHORT).show()
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
            if (smell != null && texture != null && verifiedStore != null && sharedViewModel.imageUri != null) {
                // Konversi smell dan texture ke bahasa Inggris
                val (smellEnglish, textureEnglish) = translateToEnglish(smell, texture)
                val verifiedStoreEnglish = if (verifiedStore == "Benar") "True" else "False"
                val imageUri = sharedViewModel.imageUri!!

                // Membuat MultipartBody.Part dari URI
                val imagePart = prepareFilePart(imageUri)

                if (imagePart != null) {
                    // Kirim data ke ViewModel
                    viewModel.postScanFreshness(
                        imageUri = imageUri,
                        smell = smellEnglish,
                        texture = textureEnglish,
                        verifiedStore = verifiedStoreEnglish
                    )

                    Toast.makeText(requireContext(), "Data berhasil dikirim", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "File gambar terlalu besar, maksimal 5MB", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Mohon lengkapi semua data terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun navigateToResultPage(response: ScanResponse) {
        // Ambil scanResult dari response
        val scanResult = response.data?.scanResult
        scanResult?.let {
            val imageUri = sharedViewModel.imageUri
            val intent = Intent(requireContext(), ScanResultActivity::class.java).apply {
                putExtra("SCORE", it.freshnessScore)
                putExtra("PRODUCE", it.produce)
                putExtra("IS_CONSUMABLE", it.isConsumable()) // Menentukan apakah produk layak konsumsi
                putExtra("IMAGE_URI", imageUri.toString())
            }
            startActivity(intent)
        } ?: run {
            Toast.makeText(requireContext(), "Gagal mendapatkan hasil, coba lagi!", Toast.LENGTH_SHORT).show()
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

    private fun prepareFilePart(uri: Uri): MultipartBody.Part? {
        val contentResolver = requireContext().contentResolver
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "upload_image.jpg")

        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // Validasi ukuran file
        val fileSizeInMB = file.length() / (1024 * 1024)
        if (fileSizeInMB > 5) {
            return null // File terlalu besar
        }

        val requestBody = RequestBody.create(
            contentResolver.getType(uri)?.toMediaTypeOrNull(),
            file
        )
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }
}
