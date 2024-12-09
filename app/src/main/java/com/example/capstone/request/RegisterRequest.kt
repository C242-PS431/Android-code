package com.example.capstone.request

import com.google.gson.annotations.SerializedName

data class RegisterRequest(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("content")
	val content: Content? = null
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

data class Content(

	@field:SerializedName("application/json")
	val applicationJson: ApplicationJson? = null
)

data class Ucup(

	@field:SerializedName("value")
	val value: Value? = null
)

data class ApplicationJson(

	@field:SerializedName("examples")
	val examples: Examples? = null
)

data class Data(

	@field:SerializedName("token_expires_at")
	val tokenExpiresAt: Any? = null,

	@field:SerializedName("user")
	val user: User? = null,

	@field:SerializedName("token")
	val token: String? = null
)

data class Value(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Examples(

	@field:SerializedName("ucup")
	val ucup: Ucup? = null
)
