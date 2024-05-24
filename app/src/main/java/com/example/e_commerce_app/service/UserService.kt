package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.Cart
import com.example.e_commerce_app.model.ResponseData
import com.example.e_commerce_app.model.Shop
import com.example.e_commerce_app.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @POST("/api/v1/user/register")
    suspend fun register(@Body user: User): Response<ResponseData>

    @GET("/api/v1/product")
    suspend fun getAllProduct(): Response<ResponseData>

    @GET("/api/v1/product/variant/{id}")
    suspend fun getvariant(@Path("id") ProductId:String):Response<ResponseData>

    @GET("/api/v1/category")
    suspend fun getCategories(): Response<ResponseData>

    @POST("/api/v1/registorseller")
    suspend fun RegisterSell(@Body shop: Shop): Response<ResponseData>

    @GET("/api/v1/cartlist/getcart/{uid}")
    suspend fun getCartList(@Path("uid") UserId:String): Response<ResponseData>

    @PUT("/api/v1/cartlist/updatequantitycart/{uid}")
    suspend fun updateCartList(@Body cart: Cart): Response<ResponseData>

    @PUT("/api/v1/user/finalregister/{otp}")
    suspend fun confirmOTP(@Path("otp") otp: String): Response<ResponseData>

    @POST("/api/v1/user/login")
    suspend fun login(@Body user: User): Response<ResponseData>
}