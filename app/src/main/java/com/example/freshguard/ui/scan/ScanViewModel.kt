package com.example.freshguard.ui.scan

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshguard.data.repository.ScanRepository
import com.example.freshguard.data.response.ScanResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ScanViewModel(private val scanRepository: ScanRepository) : ViewModel() {

    private val _scanResult = MutableLiveData<ScanResponse?>()
    val scanResult: LiveData<ScanResponse?> get() = _scanResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun postScanFreshness(
        imageUri: Uri, smell: String, texture: String, verifiedStore: String
    ) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = scanRepository.postScanFreshness(
                    imageUri = imageUri,
                    smell = smell,
                    texture = texture,
                    verifiedStore = verifiedStore
                )
                if (response.isSuccessful) {
                    Log.d("ScanViewModel", "Response success: ${response.body()}")
                    _scanResult.postValue(response.body())
                } else {
                    Log.e("ScanViewModel", "Response error: ${response.errorBody()?.string()}")
                    _scanResult.postValue(null)
                    _errorMessage.postValue(response.message() ?: "Gagal mendapatkan data")
                }
            } catch (e: Exception) {
                Log.e("ScanViewModel", "Exception: ${e.message}", e)
                _scanResult.postValue(null)
                _errorMessage.postValue(e.localizedMessage ?: "Terjadi kesalahan")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}
