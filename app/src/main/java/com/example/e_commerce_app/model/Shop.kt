package com.example.e_commerce_app.model

data class Shop(
    val id: Int,
    val shop_name: String,
    val Image_shop: Any?,
    val Address: String,
    val id_user: Int,
    val status: String,
    val createdAt: String,
    val updatedAt: String
)