package com.example.e_commerce_app.util

import com.example.e_commerce_app.service.*
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

    val CategoryApi: CategoryService by lazy {
        Retrofit.Builder()
            .baseUrl(Utils.USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CategoryService::class.java)
    }

    val ShopApi: ShopService by lazy {
        Retrofit.Builder()
            .baseUrl(Utils.USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShopService::class.java)
    }
    val CartApi: CartService by lazy {
        Retrofit.Builder()
            .baseUrl(Utils.USER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CartService::class.java)
    }
}