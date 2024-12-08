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

	@field:SerializedName("texture")
	val texture: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("smell")
	val smell: String? = null,

	@field:SerializedName("verified_store")
	val verifiedStore: Boolean? = null,

	@field:SerializedName("produce")
	val produce: String? = null
) {
	/**
	 * Fungsi untuk menentukan apakah buah/sayur layak dikonsumsi.
	 * Logika: freshnessScore harus >= 0.5, smell "fresh", texture "normal",
	 * dan verifiedStore harus true.
	 */
	fun isConsumable(): Boolean {
		val freshnessScoreValue = freshnessScore?.toDoubleOrNull() ?: 0.0

		return freshnessScoreValue >= 0.5 &&
				smell.equals("fresh", ignoreCase = true) &&
				texture.equals("normal", ignoreCase = true) &&
				verifiedStore == true
	}
}
