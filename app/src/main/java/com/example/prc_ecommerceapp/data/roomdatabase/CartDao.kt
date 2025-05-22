package com.example.prc_ecommerceapp.data.roomdatabase

import androidx.room.*
import kotlinx.coroutines.flow.Flow

/** Entidad que representa un artículo en el carrito de compras en la base de datos*/
@Dao
interface CartDao {

    /** Obtiene todos los artículos en el carrito de compras.*/
    @Query("SELECT * FROM cart_items")
    fun getAllCartItems(): Flow<List<CartItemEntity>>

    /** Inserta o actualiza un artículo en el carrito de compras.*/
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCartItem(item: CartItemEntity)

    /** Actualiza un artículo en el carrito de compras.*/
    @Update
    suspend fun updateCartItem(item: CartItemEntity)

    /** Elimina un artículo del carrito de compras.*/
    @Delete
    suspend fun deleteCartItem(item: CartItemEntity)

    /** Elimina todos los artículos del carrito de compras.*/
    @Query("DELETE FROM cart_items")
    suspend fun clearCart()

    /** Obtiene un artículo por su ID en el carrito de compras.*/
    @Query("SELECT * FROM cart_items WHERE productId = :productId")
    suspend fun getCartItemByProductId(productId: Int): CartItemEntity?
}