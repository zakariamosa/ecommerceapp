package com.example.ecommerceapp.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.ecommerceapp.repository.ConfigRepository
import com.example.ecommerceapp.service.ApiService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ConfigViewModelFactory(private val application: Application) : ViewModelProvider.Factory {

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://mydatasoft.net/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                    .build()
            )
            .build()
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ConfigViewModel::class.java)) {
            return ConfigViewModel(
                application,
                ConfigRepository(retrofit.create(ApiService::class.java))
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
