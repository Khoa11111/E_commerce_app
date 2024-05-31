package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.ResponseData
import com.example.e_commerce_app.request.UpdateInfoShopRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path

interface ShopService {
    @PUT("/api/v1/shop/{idShop}")
    suspend fun updateInfoShop(
        @Path("idShop") idShop: Int,
        @Body updateInfoShopRequest: UpdateInfoShopRequest
    ): Response<ResponseData>
}