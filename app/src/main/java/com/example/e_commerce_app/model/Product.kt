package com.example.e_commerce_app.model

data class Product(
    val id: Int,
    val product_name: String,
    val product_decs: String,
    val category_id: Int,
    val status: String,
    val id_shop: Int,
    val product_review: Int,
    val product_price: Int,
    val product_image: String,
    val shop: Shop?,
    val category: Category?,
    val createdAt: String,
    val updatedAt: String
)