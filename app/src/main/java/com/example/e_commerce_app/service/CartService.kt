package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.Cart
import com.example.e_commerce_app.model.ResponseData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Path

interface CartService {
    @PUT("/api/v1/cartlist/cart/{id}")
    suspend fun AddProductCart(@Path("id")id:String,@Body cart: Cart) : Response<ResponseData>

    @GET("/api/v1/cartlist/getcart/{id}")
    suspend fun GetCart(@Path("id")id: String):Response<ResponseData>
}