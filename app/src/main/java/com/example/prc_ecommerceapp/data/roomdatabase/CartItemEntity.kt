package com.example.prc_ecommerceapp.data.roomdatabase

import androidx.room.Entity
import androidx.room.PrimaryKey

/** Entidad que representa un artículo en el carrito de compras en la base de datos.
 * @param productId El ID del producto.
 * @param title El título del producto.
 * @param price El precio del producto.
 * @param image La URL de la imagen del producto.
 * @param quantity La cantidad del producto en el carrito.
 * @param tableName El nombre de la tabla de la base de datos.(En el entity)*/
@Entity(tableName = "cart_items")
data class CartItemEntity(
    @PrimaryKey val productId: Int,
    val title: String,
    val price: Double,
    val image: String,
    val quantity: Int
)
