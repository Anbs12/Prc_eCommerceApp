package com.example.prc_ecommerceapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/** CartItem representa un artículo en el carrito de compras.
 * @param product El producto asociado al artículo.
 * @param quantity La cantidad de productos en el carrito.*/
@Parcelize
data class CartItem(
    val product: Product,
    val quantity: Int = 1
) : Parcelable {
    val totalPrice: Double
        get() = product.price * quantity
}
