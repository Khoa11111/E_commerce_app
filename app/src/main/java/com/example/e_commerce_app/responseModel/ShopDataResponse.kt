package com.example.e_commerce_app.responseModel

data class ShopDataResponse(
    val err: Int,
    val mes: String,
    val userData: UserDataResponse
)