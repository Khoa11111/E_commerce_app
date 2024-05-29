package com.example.e_commerce_app.model

data class ResponseData(
    val err: Int,
    val mes: String,
    val productData: ProductData?,
    val categoryData: CategoryData?,
    val cartData: CartData?,
    val userData: User?,
    val registorSellerData: RegistorSellerData?,
    val emailUser: String?
)