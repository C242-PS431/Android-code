package com.example.capstone.response

import com.google.gson.annotations.SerializedName

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

data class Data(

    @field:SerializedName("token_expires_at")
    val tokenExpiresAt: Any? = null,

    @field:SerializedName("user")
    val user: User? = null,

    @field:SerializedName("token")
    val token: String? = null
)
