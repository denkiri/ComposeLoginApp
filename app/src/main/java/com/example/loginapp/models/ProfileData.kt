package com.example.loginapp.models
data class ProfileData(
    val error: Boolean,
    val message: String,
    val profile: Profile,
    val status: Int,
    val token: String
)