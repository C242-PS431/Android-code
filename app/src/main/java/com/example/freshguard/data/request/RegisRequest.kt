package com.example.freshguard.data.request

data class RegisRequest (
    val name: String,
    val username: String,
    val password: String
)
data class LoginRequest(
    val username: String,
    val password: String
)