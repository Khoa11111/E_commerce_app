package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.ResponseData
import retrofit2.Response
import retrofit2.http.GET
import java.util.Locale.Category

interface CategoryService {
    @GET("/api/v1/category")
    suspend fun getAllCategory(): Response<ResponseData>
}