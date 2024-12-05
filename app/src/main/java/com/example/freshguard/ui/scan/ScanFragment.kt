package com.example.freshguard.ui.scan

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.freshguard.R
import com.example.freshguard.databinding.FragmentScanBinding

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menginisialisasi ViewModel berbagi data
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        // Inisialisasi ActivityResultLauncher untuk memilih gambar dari galeri
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                if (imageUri != null) {
                    try {
                        val inputStream = requireContext().contentResolver.openInputStream(imageUri)
                        val imageBytes = inputStream?.readBytes()
                        inputStream?.close()

                        if (imageBytes != null) {
                            // Konversi gambar ke Base64 dan simpan ke SharedViewModel
                            sharedViewModel.imageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT)
                            binding.imageScan.setImageURI(imageUri)
                        } else {
                            Toast.makeText(requireContext(), "Gagal membaca gambar", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(requireContext(), "Terjadi kesalahan saat membaca gambar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Aksi ketika tombol galeri ditekan
        binding.buttonGallery.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        }

        // Aksi ketika tombol validasi ditekan
        binding.buttonValidation.setOnClickListener {
            // Navigasi ke ValidationFragment
            findNavController().navigate(R.id.action_navigation_scan_to_validationFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Mencegah memory leak
    }
}
