package com.example.ecommerceapp.repository

import com.example.ecommerceapp.model.ApiResponse
import com.example.ecommerceapp.service.ApiService

// ConfigRepository.kt
class ConfigRepository(private val apiService: ApiService) {
    suspend fun fetchBaseUrl(phoneNumber: String): Result<ApiResponse> {
        return try {
            Result.success(apiService.getBaseUrl(phoneNumber))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}