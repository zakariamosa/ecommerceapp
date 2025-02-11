package com.example.ecommerceapp.service

import com.example.ecommerceapp.model.ApiResponse
import com.example.ecommerceapp.model.ApiResponseItem
import retrofit2.http.GET
import retrofit2.http.Query

// ApiService.kt
interface ApiService {
    @GET("CustomerData/GetMobileAppUrlByClientPhone")
    suspend fun getBaseUrl(
        @Query("phonenumber") phoneNumber: String
    ): List<ApiResponseItem>
}