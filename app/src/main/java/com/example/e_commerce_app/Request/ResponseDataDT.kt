package com.example.e_commerce_app.Request

data class ResponseDataDT(
    val err: Int,
    val mes: String,
    val productData: ProductData?
)