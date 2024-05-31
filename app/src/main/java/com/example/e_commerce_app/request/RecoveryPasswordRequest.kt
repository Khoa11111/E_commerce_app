package com.example.e_commerce_app.request

data class RecoveryPasswordRequest(
    val email: String,
    val token: Int,
    val newPassword: String
)
