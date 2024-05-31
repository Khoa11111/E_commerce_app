package com.example.e_commerce_app.model

data class ProductX(
    val category_id: Int,
    val createdAt: String,
    val id: Int,
    val id_shop: Int,
    val product_decs: String,
    val product_image: String,
    val product_name: String,
    val product_price: Int,
    val product_review: Any,
    val status: String,
    val updatedAt: String
)