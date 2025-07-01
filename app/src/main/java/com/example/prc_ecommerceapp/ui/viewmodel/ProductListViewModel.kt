package com.example.prc_ecommerceapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prc_ecommerceapp.data.model.Product
import com.example.prc_ecommerceapp.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProductListUiState(
    val products: List<Product> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/** ViewModel para la pantalla de lista de productos.
 * @param repository Repositorio de la tienda.*/
@HiltViewModel
class ProductListViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {

    /** Estado de la pantalla de lista de productos. */
    private val _uiState = MutableStateFlow(ProductListUiState())
    val uiState: StateFlow<ProductListUiState> = _uiState.asStateFlow()

    /** Lista de categorías. */
    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories: StateFlow<List<String>> = _categories.asStateFlow()

    /** Lista de elementos en el carrito de compras.*/
    val cartItems = repository.getCartItems()

    //Carga los productos y las categorías al inicializar el ViewModel.
    init {
        loadProducts()
        loadCategories()
    }

    /** Carga la lista de productos. */
    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getProducts().fold(
                onSuccess = { products ->
                    _uiState.value = _uiState.value.copy(
                        products = products,
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }

    /** Carga la lista de categorías. */
    private fun loadCategories() {
        viewModelScope.launch {
            repository.getCategories().fold(
                onSuccess = { categories ->
                    _categories.value = listOf("All") + categories
                },
                onFailure = { /* Handle error silently */ }
            )
        }
    }

    /** Filtra los productos por categoría.
     * @param category Categoría a filtrar.*/
    fun filterByCategory(category: String) {
        // Si la categoría es "All", carga todos los productos
        if (category == "All") {
            loadProducts()
            return
        }

        // Si no, carga los productos filtrados por categoria
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getProductsByCategory(category).fold(
                onSuccess = { products ->
                    _uiState.value = _uiState.value.copy(
                        products = products,
                        isLoading = false,
                        error = null
                    )
                },
                onFailure = { error ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = error.message
                    )
                }
            )
        }
    }

    /** Agrega un producto al carrito de compras.
     * @param product Producto a agregar.*/
    fun addToCart(product: Product) {
        viewModelScope.launch {
            repository.addToCart(product)
        }
    }
}
