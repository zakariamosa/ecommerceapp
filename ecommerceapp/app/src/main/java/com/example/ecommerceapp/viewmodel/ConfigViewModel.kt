package com.example.ecommerceapp.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.preference.PreferenceManager
import com.example.ecommerceapp.repository.ConfigRepository
import kotlinx.coroutines.launch

/*
class ConfigViewModel(application: Application) : AndroidViewModel(application) {
    private val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(application)

    private val _editableBaseUrl = mutableStateOf(
        sharedPreferences.getString("myBaseURL", "https://default.api/") ?: "https://default.api/"
    )
    val editableBaseUrl: String
        get() = _editableBaseUrl.value

    fun updateEditableUrl(newUrl: String) {
        _editableBaseUrl.value = newUrl
    }

    fun saveBaseUrl() {
        sharedPreferences.edit().putString("myBaseURL", _editableBaseUrl.value).apply()
    }

    fun isValidUrl(url: String): Boolean {
        val urlPattern = "^(https?://)([\\w-]+\\.)+[\\w-]+(/[\\w-./?%&=]*)?$"
        return url.matches(urlPattern.toRegex())
    }
}*/
// ConfigViewModel.kt
class ConfigViewModel(
    application: Application,
    private val repository: ConfigRepository
) : AndroidViewModel(application) {

    private val _phoneNumber = mutableStateOf("")
    val phoneNumber: String get() = _phoneNumber.value

    private val _baseUrl = mutableStateOf(
        PreferenceManager.getDefaultSharedPreferences(application)
            .getString("myBaseURL", "") ?: ""
    )
    val baseUrl: String get() = _baseUrl.value

    private val _isLoading = mutableStateOf(false)
    val isLoading: Boolean get() = _isLoading.value

    private val _errorMessage = mutableStateOf<String?>(null)
    val errorMessage: String? get() = _errorMessage.value

    fun updatePhoneNumber(newNumber: String) {
        _phoneNumber.value = newNumber
    }

    fun validatePhoneNumber(): Boolean {
        return phoneNumber.matches(Regex("^01[0-9]{9}\$")) // Egyptian phone number format
    }

    fun fetchBaseUrl() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            val result = repository.fetchBaseUrl(phoneNumber)

            result.onSuccess { response ->
                _baseUrl.value = response[0].mobileAppUrl
                saveBaseUrl(response[0].mobileAppUrl)
            }.onFailure { error ->
                _errorMessage.value = "Connection failed: ${error.localizedMessage}"
            }

            _isLoading.value = false
        }
    }

    private fun saveBaseUrl(url: String) {
        _baseUrl.value = url
        PreferenceManager.getDefaultSharedPreferences(getApplication())
            .edit()
            .putString("myBaseURL", url)
            .apply()
    }
}