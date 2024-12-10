package com.example.freshguard.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: DataLogin? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class UserLogin(

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("username")
	val username: String? = null
)

data class DataLogin(

	@field:SerializedName("token_expires_at")
	val tokenExpiresAt: Any? = null,

	@field:SerializedName("user")
	val user: UserLogin? = null,

	@field:SerializedName("token")
	val token: String? = null
)
