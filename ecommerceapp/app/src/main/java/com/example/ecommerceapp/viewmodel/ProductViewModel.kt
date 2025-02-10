package com.example.ecommerceapp.viewmodel

// ProductViewModel.kt
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ecommerceapp.model.CartItem
import com.example.ecommerceapp.model.Product
import com.example.ecommerceapp.repository.ProductRepository

class ProductViewModel : ViewModel() {

    // Repository instance (manual instantiation; no DI frameworks used)
    private val repository = ProductRepository()

    // LiveData for the list of products
    private val _products = MutableLiveData<List<Product>>()
    val products: LiveData<List<Product>>
        get() = _products

    // LiveData for the shopping cart, a map of product id to CartItem
    private val _cartItems = MutableLiveData<MutableMap<Int, CartItem>>(mutableMapOf())
    val cartItems: LiveData<MutableMap<Int, CartItem>>
        get() = _cartItems

    init {
        // Load products from the repository
        loadProducts()
    }

    private fun loadProducts() {
        _products.value = repository.getProducts()
    }

    // Add a product to the shopping cart
    fun addToCart(product: Product) {
        // Get the current cart or an empty map
        val currentCart = _cartItems.value ?: mutableMapOf()

        // Update the product's cart entry
        val cartItem = currentCart[product.id]
        if (cartItem == null) {
            currentCart[product.id] = CartItem(product, quantity = 1)
        } else {
            cartItem.quantity++
        }

        // Create a completely new map instance and update the LiveData
        _cartItems.value = HashMap(currentCart)
        Log.d("ProductViewModel", "Cart updated: ${_cartItems.value}")
    }


    // Remove a product from the shopping cart
    fun removeFromCart(product: Product) {
        val currentCart = _cartItems.value ?: mutableMapOf()
        val cartItem = currentCart[product.id]
        if (cartItem != null) {
            if (cartItem.quantity > 1) {
                cartItem.quantity--
            } else {
                currentCart.remove(product.id)
            }
        }
        _cartItems.value = currentCart
    }

    // Optionally, clear the entire cart
    fun clearCart() {
        _cartItems.value = mutableMapOf()
    }
}
