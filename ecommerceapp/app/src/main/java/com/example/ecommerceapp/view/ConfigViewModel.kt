// ConfigViewModel.kt
package com.example.ecommerceapp.viewmodel

import android.app.Application
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.application
import androidx.preference.PreferenceManager

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
}