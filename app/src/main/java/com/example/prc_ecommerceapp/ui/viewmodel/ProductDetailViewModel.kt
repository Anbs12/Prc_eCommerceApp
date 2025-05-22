package com.example.prc_ecommerceapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prc_ecommerceapp.data.model.Product
import com.example.prc_ecommerceapp.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/** Estado de la pantalla de detalles del producto. */
data class ProductDetailUiState(
    val product: Product? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

/** ViewModel para la pantalla de detalles del producto.
 * @param repository Repositorio de la tienda.*/
@HiltViewModel
class ProductDetailViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {

    /** Estado de la pantalla de detalles del producto. */
    private val _uiState = MutableStateFlow(ProductDetailUiState())
    val uiState: StateFlow<ProductDetailUiState> = _uiState.asStateFlow()

    /** Carga los detalles del producto con el ID proporcionado.
     * @param id ID del producto.*/
    fun loadProduct(id: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getProduct(id).fold(
                onSuccess = { product ->
                    _uiState.value = _uiState.value.copy(
                        product = product,
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

    /** Agrega el producto al carrito de compras. */
    fun addToCart() {
        viewModelScope.launch {
            _uiState.value.product?.let { product ->
                repository.addToCart(product)
            }
        }
    }
}
