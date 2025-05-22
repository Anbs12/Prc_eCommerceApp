package com.example.prc_ecommerceapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.prc_ecommerceapp.data.repository.ShopRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/** ViewModel para la pantalla de carrito de compras.
 * @param repository Repositorio de la tienda.*/
@HiltViewModel
class CartViewModel @Inject constructor(
    private val repository: ShopRepository
) : ViewModel() {

    /** Lista de elementos en el carrito de compras.*/
    val cartItems = repository.getCartItems()

    /** Actualiza la cantidad de un elemento en el carrito.*/
    fun updateQuantity(productId: Int, quantity: Int) {
        viewModelScope.launch {
            repository.updateCartItemQuantity(productId, quantity)
        }
    }

    /** Elimina un elemento del carrito.*/
    fun removeItem(productId: Int) {
        viewModelScope.launch {
            repository.removeFromCart(productId)
        }
    }

    /** Limpia el carrito de compras.*/
    fun clearCart() {
        viewModelScope.launch {
            repository.clearCart()
        }
    }
}