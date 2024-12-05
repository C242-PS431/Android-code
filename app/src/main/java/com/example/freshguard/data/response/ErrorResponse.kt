package com.example.freshguard.data.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("errors")
	val errors: Errors? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Errors(

	@field:SerializedName("image")
	val image: List<String?>? = null
)
