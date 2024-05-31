package com.example.e_commerce_app.model

data class Variant(
    val Product: ProductX,
    val createdAt: String,
    val id: Int,
    val id_product: Int,
    val updatedAt: String,
    val variant_image: String?,
    val variant_name: String,
    val variant_numbersell: Int,
    val variant_price: Int,
    val variant_selled: Int
)