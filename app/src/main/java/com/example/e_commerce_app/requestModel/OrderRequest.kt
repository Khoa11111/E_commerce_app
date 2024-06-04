package com.example.e_commerce_app.requestModel

data class OrderRequest(
    val id_user: Int,
    val Phone: Int,
    val Address_ship: String,
    val items: ArrayList<ItemRequest>,
    val payment_method: String
)