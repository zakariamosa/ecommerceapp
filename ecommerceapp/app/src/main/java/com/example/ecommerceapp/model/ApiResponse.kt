package com.example.ecommerceapp.model

import com.google.gson.annotations.SerializedName

// ApiResponse.kt
data class ApiResponseItem(
    @SerializedName("Name") val name: String,
    @SerializedName("MobileAppUrl") val mobileAppUrl: String
)

typealias ApiResponse = List<ApiResponseItem> // Expecting a list
