package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.Cart
import com.example.e_commerce_app.model.ResponseData
import retrofit2.Response
import retrofit2.http.*

interface CartService {
    @PUT("/api/v1/cartlist/cart/{id}")
    suspend fun AddProductCart(@Path("id")id:String,@Body cart: Cart) : Response<ResponseData>

    @GET("/api/v1/cartlist/getcart/{id}")
    suspend fun GetCart(@Path("id")id: Int):Response<ResponseData>

    @DELETE("/api/v1/cartlist/remove-cart/{id}/{idVariant}")
    suspend fun DeleteCart(@Path("id")id: String,@Path("idVariant")idVariant:String):Response<ResponseData>
}