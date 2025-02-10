package com.example.ecommerceapp.model

// CartItem.kt
data class CartItem(
    val product: Product,
    var quantity: Int = 1
)
