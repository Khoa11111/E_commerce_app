package com.example.e_commerce_app.service

import com.example.e_commerce_app.model.*
import com.example.e_commerce_app.requestModel.EmailRequest
import com.example.e_commerce_app.requestModel.RecoveryPasswordRequest
import retrofit2.Response
import retrofit2.http.*

interface UserService {
    @POST("/api/v1/user/register")
    suspend fun register(@Body user: User): Response<ResponseData>

    @PUT("/api/v1/user/finalregister/{otp}")
    suspend fun confirmOTP(@Path("otp") otp: String): Response<ResponseData>

    @POST("/api/v1/user/login")
    suspend fun login(@Body user: User): Response<ResponseData>

    @POST("/api/v1/registorseller")
    suspend fun RegSeller(@Body registorSellerData: RegistorSellerData): Response<ResponseData>

    @PUT("/api/v1/registorseller/finalRegistorSeller/{otp}")
    suspend fun confirmOTPSeller(@Path("otp") otp: String): Response<ResponseData>

    @GET("/api/v1/user/current/{id}")
    suspend fun getUserSearch(@Path("id") id: String): Response<ResponseData>

    @PUT("/api/v1/user/{id}")
    suspend fun UpdateUser(@Path("id") id: String, @Body user: User): Response<ResponseData>

    // send otp to recovery password
    @POST("/api/v1/user/forgotpassword")
    suspend fun sendOtpRecoveryPassword(@Body email: EmailRequest): Response<ResponseData>

    // recovery password
    @PUT("/api/v1/user/resetpassword")
    suspend fun recoveryPassword(@Body recoveryPasswordRequest: RecoveryPasswordRequest): Response<ResponseData>

}