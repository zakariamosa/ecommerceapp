package com.example.ecommerceapp.view

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*


import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ecommerceapp.model.Product
import com.example.ecommerceapp.viewmodel.ProductViewModel
import com.example.ecommerceapp.ui.theme.EcommerceappTheme
import com.example.ecommerceapp.viewmodel.ConfigViewModel
import com.example.ecommerceapp.viewmodel.ConfigViewModelFactory
import kotlinx.coroutines.delay

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EcommerceappTheme {
                // MainScreen now handles view switching between products and cart.
                MainScreen()
            }
        }
    }
}

enum class Screen {
    PRODUCTS, CART, SETTINGS
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(productViewModel: ProductViewModel = viewModel()) {
    // Observe products and cart data from the ViewModel.
    val products by productViewModel.products.observeAsState(emptyList())
    val cartItems by productViewModel.cartItems.observeAsState(initial = mutableMapOf())
    var currentScreen by remember { mutableStateOf(Screen.PRODUCTS) }

    // A flag to toggle between the product list and the shopping cart.
    var showCart by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("E-commerce App") },
                actions = {
                    IconButton(onClick = {
                        currentScreen = if (currentScreen == Screen.CART) Screen.PRODUCTS else Screen.CART
                    }) {
                        Icon(Icons.Filled.ShoppingCart, "Cart")
                    }
                    IconButton(onClick = { currentScreen = Screen.SETTINGS }) {
                        Icon(Icons.Filled.Settings, "Settings")
                    }
                }
            )
        }
    ) {  innerPadding ->
        when (currentScreen) {
            Screen.PRODUCTS -> ProductListScreen(
                products = products,
                onAddToCart = { productViewModel.addToCart(it) },
                modifier = Modifier.padding(innerPadding)
            )
            Screen.CART -> ShoppingCartScreen(
                cartItems = cartItems,
                modifier = Modifier.padding(innerPadding)
            )
            Screen.SETTINGS -> SettingsScreen(
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
fun ProductListScreen(
    products: List<Product>,
    onAddToCart: (Product) -> Unit,
    modifier: Modifier = Modifier
) {
    // Display the list of products.
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Products", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        products.forEach { product ->
            ProductItem(
                product = product,
                onAddToCart = { onAddToCart(product) }
            )
        }
    }
}

@Composable
fun ShoppingCartScreen(
    cartItems: Map<Int, com.example.ecommerceapp.model.CartItem>,
    modifier: Modifier = Modifier
) {
    // Calculate the total quantity of items in the cart.
    val totalQuantity = cartItems.values.sumOf { it.quantity }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(text = "Shopping Cart", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))
        // Display the total items.
        Text(text = "Total Items: $totalQuantity", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(8.dp))
        // If the cart is empty, show an appropriate message.
        if (cartItems.isEmpty()) {
            Text(text = "Cart is empty")
        } else {
            cartItems.values.forEach { cartItem ->
                Text(text = "${cartItem.product.name} x ${cartItem.quantity}")
            }
        }
    }
}

@Composable
fun ProductItem(product: Product, onAddToCart: () -> Unit) {
    // Display a single product with an "Add to Cart" button.
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(text = product.name, style = MaterialTheme.typography.titleMedium)
                Text(text = product.description, style = MaterialTheme.typography.bodyMedium)
                Text(text = "$${product.price}", style = MaterialTheme.typography.bodyLarge)
            }
            Button(onClick = onAddToCart) {
                Text("Add to Cart")
            }
        }
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current.applicationContext as Application // Get application context

    val viewModel: ConfigViewModel = viewModel(
        factory = ConfigViewModelFactory(context) // Pass application instance
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Phone Number Input
        OutlinedTextField(
            value = viewModel.phoneNumber,
            onValueChange = { viewModel.updatePhoneNumber(it) },
            label = { Text("Phone Number") },
            placeholder = { Text("01xxxxxxxxx") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth(),
            isError = viewModel.phoneNumber.isNotBlank() && !viewModel.validatePhoneNumber(),
            supportingText = {
                if (viewModel.phoneNumber.isNotBlank() && !viewModel.validatePhoneNumber()) {
                    Text("Invalid Egyptian phone number format")
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Connect Button
        Button(
            onClick = { viewModel.fetchBaseUrl() },
            modifier = Modifier.align(Alignment.End),
            enabled = viewModel.validatePhoneNumber() && !viewModel.isLoading
        ) {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Connect")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Base URL Display
        if (viewModel.baseUrl.isNotEmpty()) {
            Column {
                Text("Configured Base URL:", style = MaterialTheme.typography.titleSmall)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = viewModel.baseUrl,
                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.primary)
                )
            }
        }

        // Error Message
        viewModel.errorMessage?.let { error ->
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = error,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

