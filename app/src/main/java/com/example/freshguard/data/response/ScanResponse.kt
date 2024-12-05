package com.example.freshguard.data.response

import com.google.gson.annotations.SerializedName

data class ScanResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("scan_result")
	val scanResult: ScanResult? = null
)

data class ScanResult(

	@field:SerializedName("freshness_score")
	val freshnessScore: String? = null,

	@field:SerializedName("scanned_at")
	val scannedAt: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("produce")
	val produce: String? = null,

	@field:SerializedName("texture")
	val texture: String? = null,

	@field:SerializedName("smell")
	val smell: String? = null,

	@field:SerializedName("verified_store")
	val verifiedStore: Boolean? = null
) {
	// Fungsi untuk menentukan apakah produk layak konsumsi
	fun isConsumable(): Boolean {
		// Menentukan berdasarkan skor kesegaran, tekstur, dan bau
		return (
				(freshnessScore?.toIntOrNull() ?: 0) >= 50 && // Misalnya skor lebih dari 50 dianggap layak
						texture != "bad" && // Jika teksturnya buruk
						smell != "bad" && // Jika baunya buruk
						verifiedStore == true // Jika berasal dari toko yang terverifikasi
				)
	}

}
