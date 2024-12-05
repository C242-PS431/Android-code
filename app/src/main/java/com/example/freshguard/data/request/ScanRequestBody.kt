package com.example.freshguard.data.request

data class ScanRequestBody(
    val image: String,  // Gambar dalam format Base64
    val smell: String,  // "fresh", "neutral", "rotten"
    val texture: String, // "hard", "normal", "soft"
    val verifiedStore: String   // "true" atau "false"
)