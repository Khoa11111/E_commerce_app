package com.example.e_commerce_app.service

import com.example.e_commerce_app.Request.ResponseDataDT
import com.example.e_commerce_app.model.Product
import com.example.e_commerce_app.model.ResponseData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ProductService {

    @GET("/api/v1/product")
    suspend fun getAllProduct(@Query("status") status:String): Response<ResponseData>

    @GET("/api/v1/product/{id}")
    suspend fun getDetailPr(@Path("id")id:String):Response<ResponseDataDT>

    @GET("/api/v1/product/variantProduct/{id}")
    suspend fun GetVariant(@Path("id")id:String):Response<ResponseData>
//
//    @POST("/api/v1/product/variant/{id}")
//    suspend fun CreateVariant(@Body variantData: VariantData): Response<ResponseData>

    @GET("/api/v1/product")
    suspend fun GetProduct_ID(@Query("id")id: String?):Response<ResponseData>

    @POST("/api/v1/product")
    suspend fun CreateProduct(@Body product: Product) : Response<ResponseData>


}