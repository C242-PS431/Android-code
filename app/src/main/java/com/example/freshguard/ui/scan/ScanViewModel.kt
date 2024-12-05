package com.example.freshguard.ui.scan

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshguard.data.repository.ScanRepository
import com.example.freshguard.data.request.ScanRequestBody
import com.example.freshguard.data.response.ScanResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ScanViewModel(private val scanRepository: ScanRepository) : ViewModel() {

    // LiveData untuk menyimpan response dari API
    private val _scanResult = MutableLiveData<ScanResponse?>()
    val scanResult: LiveData<ScanResponse?> get() = _scanResult

    fun postScanFreshness (token: String, scanRequestBody: ScanRequestBody) {
        viewModelScope.launch {
            try {
                val response = scanRepository.postScanFreshness(token, scanRequestBody)
                if (response.isSuccessful) {
                    _scanResult.postValue(response.body())
                } else {
                    _scanResult.postValue(null)
                }
            } catch (e: Exception) {
                _scanResult.postValue(null)
            }
        }
    }
}