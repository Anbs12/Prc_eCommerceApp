package com.example.prc_ecommerceapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prc_ecommerceapp.ui.screens.CartScreen
import com.example.prc_ecommerceapp.ui.screens.CheckoutScreen
import com.example.prc_ecommerceapp.ui.screens.OrderSuccessScreen
import com.example.prc_ecommerceapp.ui.screens.ProductDetailScreen
import com.example.prc_ecommerceapp.ui.screens.ProductListScreen
import com.example.prc_ecommerceapp.ui.theme.Prc_eCommerceAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Prc_eCommerceAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Prc_eCommerceApp()
                }
            }
        }
    }
}


/** Composable principal de la aplicación.
 *
 * La navegacion se maneja dentro de este composable. */
@Composable
fun Prc_eCommerceApp() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "products"
    ) {
        composable("products") {
            ProductListScreen(
                onProductClick = { product ->
                    navController.navigate("product_detail/${product.id}")
                },
                onCartClick = {
                    navController.navigate("cart")
                }
            )
        }

        composable("cart") {
            CartScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onCheckoutClick = {
                    navController.navigate("checkout")
                }
            )
        }

        composable("product_detail/{productId}") { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId")?.toIntOrNull()
            if (productId != null) {
                ProductDetailScreen(
                    productId = productId,
                    onBackClick = {
                        navController.popBackStack()
                    },
                    onAddToCart = {
                        // Add to cart and show snackbar
                    }
                )
            }
        }

        composable("checkout") {
            CheckoutScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                onOrderComplete = {
                    navController.navigate("order_success") {
                        popUpTo("products") { inclusive = false }
                    }
                }
            )
        }

        composable("order_success") {
            OrderSuccessScreen(
                onContinueShopping = {
                    navController.navigate("products") {
                        popUpTo("products") { inclusive = true }
                    }
                }
            )
        }
    }
}
