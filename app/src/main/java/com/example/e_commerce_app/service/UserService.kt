package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.ResponseData
import com.example.e_commerce_app.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface UserService {
    @POST("/api/v1/user/register")
    suspend fun register(@Body user: User): Response<ResponseData>

    @PUT("/api/v1/user/finalregister/{otp}")
    suspend fun confirmOTP(@Path("otp") otp: String): Response<ResponseData>

    @POST("/api/v1/user/login")
    suspend fun login(@Body user: User): Response<ResponseData>
}