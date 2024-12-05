package com.example.freshguard.data.response

import com.google.gson.annotations.SerializedName

data class ScanTrackResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
