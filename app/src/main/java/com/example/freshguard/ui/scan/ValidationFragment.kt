package com.example.freshguard.ui.scan

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.freshguard.data.retrofit.ApiCon
import com.example.freshguard.data.retrofit.ApiConfig
import com.example.freshguard.databinding.FragmentValidationBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

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
            ScanViewModelFactory(requireContext(), ScanRepository(ApiCon.apiScanService, requireContext()))
        ).get(ScanViewModel::class.java)

        viewModel.scanResult.observe(viewLifecycleOwner) { response ->
            if (response != null) {
                navigateToResultPage(response)
            } else {
                viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
                    if (error != null) {
                        Toast.makeText(requireContext(), "Gagal mendapatkan hasil: $error", Toast.LENGTH_SHORT).show()
                    }
                }

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
                val verifiedStoreEnglish = if (verifiedStore == "Benar") "true" else "false"
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
            val intent = Intent(requireContext(), ScanResultActivity::class.java).apply {
                putExtra("SCORE", it.freshnessScore)
                putExtra("PRODUCE", it.produce)
//                putExtra("IS_CONSUMABLE", it.isConsumable()) // Menentukan apakah produk layak konsumsi
            }
            startActivity(intent)
        } ?: run {
            Toast.makeText(requireContext(), "Gagal mendapatkan hasil, coba lagi!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun translateToEnglish(smell: String?, texture: String?): Pair<String, String> {
        val smellMap = mapOf(
            "Segar" to "fresh",
            "Busuk" to "rotten",
            "Netral" to "neutral"
        )

        val textureMap = mapOf(
            "Keras" to "hard",
            "Lembek" to "soft",
            "Normal" to "normal"
        )

        // Berikan nilai default "neutral" untuk smell dan "normal" untuk texture jika null
        return Pair(
            smellMap[smell] ?: "neutral",
            textureMap[texture] ?: "normal"
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

        // Menghitung ukuran file sebelum kompresi
        var fileSizeInMB = file.length() / (1024 * 1024)

        // Jika ukuran file lebih dari 5MB, kita kompres menjadi 5MB
        if (fileSizeInMB > 5) {
            val compressedFile = compressImage(file, 5) // Kompres menjadi maksimal 5MB
            fileSizeInMB = compressedFile.length() / (1024 * 1024)
            file.delete() // Hapus file lama yang besar
            compressedFile.renameTo(file) // Gantilah file yang lama dengan file yang sudah dikompres
        }

        // Tidak mengembalikan null, meskipun ukuran file lebih besar dari 5MB setelah kompresi
        // Jika file masih lebih besar dari 5MB, tetap lanjutkan proses
        if (fileSizeInMB > 5) {
            Toast.makeText(requireContext(), "File gambar lebih besar dari 5MB, tapi tetap akan dikirim", Toast.LENGTH_SHORT).show()
        }

        // Membuat RequestBody untuk gambar
        val requestBody = RequestBody.create(
            contentResolver.getType(uri)?.toMediaTypeOrNull(),
            file
        )

        // Mengembalikan file sebagai MultipartBody.Part untuk dikirim ke API
        return MultipartBody.Part.createFormData("image", file.name, requestBody)
    }

    // Fungsi untuk mengompres gambar agar ukuran file tidak lebih dari 5MB
    private fun compressImage(imageFile: File, maxSizeMB: Int): File {
        val bitmap = BitmapFactory.decodeFile(imageFile.absolutePath)

        // Tentukan kualitas kompresi
        val outputStream = ByteArrayOutputStream()
        var quality = 100 // Mulai dengan kualitas gambar maksimal

        // Kompres gambar hingga ukuran yang lebih kecil, sesuaikan kualitas jika perlu
        do {
            outputStream.reset() // Reset output stream agar tidak menambah data sebelumnya
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream)

            // Menghitung ukuran file setelah kompresi
            val compressedSizeInMB = outputStream.size() / (1024 * 1024)
            if (compressedSizeInMB <= maxSizeMB) {
                break // Jika sudah mencapai ukuran yang diinginkan, keluar dari loop
            }

            // Kurangi kualitas kompresi jika ukuran masih lebih besar dari maxSizeMB
            quality -= 5
        } while (quality > 10) // Stop jika kualitas terlalu rendah (di bawah 10%)

        // Menyimpan gambar yang sudah terkompres
        val compressedFile = File(requireContext().cacheDir, "compressed_image.jpg")
        FileOutputStream(compressedFile).use {
            it.write(outputStream.toByteArray())
        }

        return compressedFile
    }

}
