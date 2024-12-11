package com.example.freshguard.data.local

import org.osmdroid.util.GeoPoint

data class ModelStore (
    val name: String,
    val location: GeoPoint, // Lokasi toko dalam bentuk GeoPoint
    val status: String, // Status toko (misalnya "Buka" atau "Tutup")
    val alamat: String // Alamat toko
)