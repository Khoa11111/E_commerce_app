package com.example.e_commerce_app.model

data class ProductCartData(
    val category_id: Int,
    val createdAt: String,
    val id: Int,
    val id_shop: Int,
    val product_decs: String,
    val product_image: Any,
    val product_name: String,
    val product_price: Int,
    val product_review: Int,
    val status: String,
    val updatedAt: String
)