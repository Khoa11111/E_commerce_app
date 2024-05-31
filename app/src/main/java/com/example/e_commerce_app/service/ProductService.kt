package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.ResponseData
import com.example.e_commerce_app.model.variantData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ProductService {

    @GET("/api/v1/product")
    suspend fun getAllProduct(): Response<ResponseData>

    @GET("/api/v1/product/{id}")
    suspend fun getDetailPr():Response<ResponseData>

    @GET("/api/v1/product/variant/{id}")
    suspend fun GetVariantL(@Path("id")id:String):Response<ResponseData>

    @POST("/api/v1/product/variant/{id}")
    suspend fun CreateVariant(@Body variantData: variantData): Response<ResponseData>

    @GET("/api/v1/product?id_shop={id}")
    suspend fun GetProduct_ID(@Path("id")id: String):Response<ResponseData>
}