package com.example.freshguard.ui.scan

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.freshguard.R
import com.example.freshguard.databinding.FragmentScanBinding

class ScanFragment : Fragment() {

    private var _binding: FragmentScanBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var galleryLauncher: ActivityResultLauncher<Intent>
    private lateinit var cameraLauncher: ActivityResultLauncher<Intent>
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Inisialisasi ActivityResultLauncher untuk memilih gambar dari galeri
        galleryLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageUri = result.data?.data
                if (imageUri != null) {
                    sharedViewModel.imageUri = imageUri
                    binding.imageScan.setImageURI(imageUri)
                } else {
                    Toast.makeText(requireContext(), "Tidak ada gambar yang dipilih", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Gagal membuka galeri", Toast.LENGTH_SHORT).show()
            }
        }

        // Inisialisasi ActivityResultLauncher untuk mengambil gambar dengan kamera
        cameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val imageBitmap = result.data?.extras?.get("data") as? Bitmap
                if (imageBitmap != null) {
                    // Simpan gambar di cache dan dapatkan URI
                    val imageUri = saveImageToCache(requireContext(), imageBitmap)
                    if (imageUri != null) {
                        sharedViewModel.imageUri = imageUri
                        binding.imageScan.setImageURI(imageUri)
                    } else {
                        Toast.makeText(requireContext(), "Gagal menyimpan gambar", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Gagal mengambil gambar", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(requireContext(), "Gagal membuka kamera", Toast.LENGTH_SHORT).show()
            }
        }

        // Inisialisasi ActivityResultLauncher untuk meminta izin
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val storageGranted = when {
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                    permissions[Manifest.permission.READ_MEDIA_IMAGES] == true
                else ->
                    permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == true
            }
            val cameraGranted = permissions[Manifest.permission.CAMERA] == true

            if (storageGranted && cameraGranted) {
                if (binding.buttonGallery.isPressed) {
                    openGallery()
                } else if (binding.buttonCamera.isPressed) {
                    openCamera()
                }
            } else {
                Toast.makeText(requireContext(), "Izin akses ditolak", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateView(
        inflater: android.view.LayoutInflater, container: android.view.ViewGroup?,
        savedInstanceState: Bundle?
    ): android.view.View {
        _binding = FragmentScanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: android.view.View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sharedViewModel = ViewModelProvider(requireActivity())[SharedViewModel::class.java]

        // Aksi tombol galeri
        binding.buttonGallery.setOnClickListener {
            if (hasStoragePermission()) {
                openGallery()
            } else {
                requestStoragePermission()
            }
        }

        // Aksi tombol kamera
        binding.buttonCamera.setOnClickListener {
            if (hasCameraPermission()) {
                openCamera()
            } else {
                requestCameraPermission()
            }
        }

        // Aksi tombol validasi
        binding.buttonValidation.setOnClickListener {
            if (sharedViewModel.imageUri != null) {
                findNavController().navigate(R.id.action_navigation_scan_to_validationFragment)
            } else {
                Toast.makeText(requireContext(), "Pilih gambar terlebih dahulu", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun hasStoragePermission(): Boolean {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) == PackageManager.PERMISSION_GRANTED
            else ->
                ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun hasCameraPermission(): Boolean {
        return ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestStoragePermission() {
        val permissions = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES)
            else ->
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
        }
        requestPermissionLauncher.launch(permissions)
    }

    private fun requestCameraPermission() {
        requestPermissionLauncher.launch(arrayOf(Manifest.permission.CAMERA))
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryLauncher.launch(intent)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        cameraLauncher.launch(intent)
    }

    private fun saveImageToCache(context: Context, bitmap: Bitmap): Uri? {
        return try {
            val fileName = "image_${System.currentTimeMillis()}.png"
            val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
            outputStream.close()
            val filePath = context.filesDir.path + "/" + fileName
            Uri.parse("file://$filePath")
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
