package com.example.ecommerceapp.repository

import com.example.ecommerceapp.model.Product

// ProductRepository.kt
class ProductRepository {

    // Returns a list of mock products
    fun getProducts(): List<Product> {
        return listOf(
            Product(1, "Smartphone", "Latest smartphone with great features", 699.99),
            Product(2, "Laptop", "High performance laptop for work and play", 1199.99),
            Product(3, "Headphones", "Noise cancelling headphones", 199.99),
            Product(4, "Smartwatch", "Keep track of your fitness and notifications", 249.99)
        )
    }
}
