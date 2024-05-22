package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.ResponseData
import retrofit2.Response
import retrofit2.http.GET

interface ProductService {

    @GET("/api/v1/category")
    suspend fun getAllCategory(): Response<ResponseData>
}