package com.example.e_commerce_app.model

data class ResponseData(
    val err: Int,
    val mes: String,
    val productData: ProductData?,
    val userData: UserData?
)