package com.example.freshguard.data.response

import com.google.gson.annotations.SerializedName

data class RegisResponse(

	@field:SerializedName("data")
	val data: DataRegis? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataRegis(

	@field:SerializedName("token_expires_at")
	val tokenExpiresAt: Any? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class User(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)
