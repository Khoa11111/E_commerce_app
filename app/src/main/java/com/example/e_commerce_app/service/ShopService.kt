package com.example.e_commerce_app.service

import retrofit2.http.GET

interface ShopService {
    @GET("/api/v1/shop/getShop/{id}")
    suspend fun getShopByID()
}