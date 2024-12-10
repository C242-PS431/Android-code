package com.example.freshguard.ui.register

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.freshguard.data.repository.AuthRepository

class AuthViewModelFactory(private val application: Application, private val authRepository: AuthRepository) : ViewModelProvider.Factory {

}
