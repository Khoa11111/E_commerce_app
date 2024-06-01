package com.example.e_commerce_app.model

data class Cart(
    val createdAt: String?,
    val id: Int?,
    val id_shop: Int?,
    val maSP: Int?,
    val price: Int?,
    val productCartData: ProductCartData?,
    val shop: Shop?,
    val soLuongMua: Int?,
    val status: String?,
    val uid: Int?,
    val updatedAt: String?,
    val variant_id: Int?
)