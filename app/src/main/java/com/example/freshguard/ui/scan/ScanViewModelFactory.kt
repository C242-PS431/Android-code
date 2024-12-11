package com.example.freshguard.ui.scan

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freshguard.data.repository.ScanRepository

class ScanViewModelFactory(
    private val context: Context,
    private val repository: ScanRepository
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    // Method untuk membuat ScanViewModel dengan ScanRepository yang sudah diinjeksi
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScanViewModel::class.java)) {
            return ScanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}