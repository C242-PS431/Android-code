package com.example.freshguard.data.repository

import android.content.Context
import android.net.Uri
import com.example.freshguard.data.response.ScanResponse
import com.example.freshguard.data.retrofit.ApiScan
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class ScanRepository(private val apiScan: ApiScan, private val context: Context) {
    suspend fun postScanFreshness(
        imageUri: Uri, // Ganti dari MultipartBody.Part ke Uri
        smell: String,
        texture: String,
        verifiedStore: String
    ): Response<ScanResponse> {

        // Ubah URI menjadi File sementara
        val file = createTempFileFromUri(imageUri)

        // Buat MultipartBody.Part untuk file gambar
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val imagePart = MultipartBody.Part.createFormData("image", file.name, requestFile)

        // Buat RequestBody untuk parameter tambahan
        val smellBody = smell.toRequestBody("text/plain".toMediaTypeOrNull())
        val textureBody = texture.toRequestBody("text/plain".toMediaTypeOrNull())
        val verifiedStoreBody = verifiedStore.toRequestBody("text/plain".toMediaTypeOrNull())

        // Kirim data ke API
        return apiScan.postScanFreshness(imagePart, smellBody, textureBody, verifiedStoreBody)
    }

    // Fungsi untuk membuat file sementara dari URI
    private fun createTempFileFromUri(uri: Uri): File {
        // Buka InputStream dari URI menggunakan contentResolver
        val inputStream = context.contentResolver.openInputStream(uri)
            ?: throw IllegalArgumentException("File tidak ditemukan")

        // Buat file sementara di direktori cache aplikasi
        val tempFile = File.createTempFile("upload_", ".jpg", context.cacheDir)

        // Salin data dari inputStream ke outputStream
        val outputStream = FileOutputStream(tempFile)
        inputStream.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        return tempFile // Kembalikan file
    }
}
