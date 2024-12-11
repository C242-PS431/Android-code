package com.example.freshguard.ui.tracking

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.freshguard.data.repository.HistoryRepository
import com.example.freshguard.data.response.HistoryResponse
import kotlinx.coroutines.launch

class HistoryViewModel(private val historyRepository: HistoryRepository) : ViewModel() {
    private val _historyResult = MutableLiveData<HistoryResponse?>()
    val historyResult: LiveData<HistoryResponse?> get() = _historyResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    fun getHistoryScan(page: Int, perPage: Int) {
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = historyRepository.getHistoryScan(page, perPage)
                if (response.isSuccessful) {
                    _historyResult.postValue(response.body())
                } else {
                    _historyResult.postValue(null)
                    _errorMessage.postValue(response.message() ?: "gagal mendapatkan data")
                }
            } catch (e: Exception) {
                _historyResult.postValue(null)
                _errorMessage.postValue(e.localizedMessage ?: "terjadi kesalahan")
            } finally {
                _isLoading.postValue(false)
            }
        }
    }
}