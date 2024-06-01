package com.example.e_commerce_app.Request

data class ProductData(
    val category: Category,
    val category_id: Int,
    val createdAt: String,
    val id: Int,
    val id_shop: Int,
    val product_decs: String,
    val product_image: String,
    val product_name: String,
    val product_price: Int,
    val product_review: Any,
    val shop: Shop,
    val status: String,
    val updatedAt: String
)