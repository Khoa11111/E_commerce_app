package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.ResponseData
import com.example.e_commerce_app.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/api/v1/user/register")
    suspend fun register(@Body user: User): Response<ResponseData>
}