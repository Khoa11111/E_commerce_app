package com.example.e_commerce_app.util

import com.example.e_commerce_app.service.ProductService
import com.example.e_commerce_app.service.UserService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitInstance {
    val UserApi: UserService by lazy {
        Retrofit.Builder()
            .baseUrl(Utils.USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserService::class.java)
    }

    val ProductApi: ProductService by lazy {
        Retrofit.Builder()
            .baseUrl(Utils.USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ProductService::class.java)
    }
}