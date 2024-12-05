package com.example.freshguard.data.utils

import java.io.File
import android.util.Base64

fun convertImageToBase64(imagePath: String): String? {
    return try {
        val file = File(imagePath)
        val bytes = file.readBytes()
        Base64.encodeToString(bytes, Base64.NO_WRAP)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}