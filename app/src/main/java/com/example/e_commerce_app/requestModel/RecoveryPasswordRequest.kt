package com.example.e_commerce_app.requestModel

data class RecoveryPasswordRequest(
    val email: String,
    val token: Int,
    val newPassword: String
)
