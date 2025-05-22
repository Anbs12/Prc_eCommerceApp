package com.example.prc_ecommerceapp.data.repository

import com.example.prc_ecommerceapp.data.apiservice.ApiService
import com.example.prc_ecommerceapp.data.model.CartItem
import com.example.prc_ecommerceapp.data.model.Product
import com.example.prc_ecommerceapp.data.model.Rating
import com.example.prc_ecommerceapp.data.roomdatabase.CartDao
import com.example.prc_ecommerceapp.data.roomdatabase.CartItemEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

/** Repositorio para operaciones relacionadas con la tienda.
 * @param apiService Servicio para acceder a la API.
 * @param cartDao Dao para operaciones en el carrito de compras.*/
@Singleton
class ShopRepository @Inject constructor(
    private val apiService: ApiService,
    private val cartDao: CartDao
) {

    /** Obtiene una lista de productos.
     * @return La lista de productos si se obtienen correctamente, o un error si no se obtienen.
     * @throws Exception Si ocurre un error al obtener los productos.
     * */
    suspend fun getProducts(): Result<List<Product>> {
        return try {
            val products = apiService.getProducts()
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Obtiene un producto por su ID.
     * @param id El ID del producto.
     * @return El producto si se encuentra, o null si no se encuentra.*/
    suspend fun getProduct(id: Int): Result<Product> {
        return try {
            val product = apiService.getProduct(id)
            Result.success(product)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Obtiene una lista de categorías de productos.
     * @return La lista de categorías si se obtienen correctamente, o un error si no se obtienen.
     * @throws Exception Si ocurre un error al obtener las categorías.
     * */
    suspend fun getCategories(): Result<List<String>> {
        return try {
            val categories = apiService.getCategories()
            Result.success(categories)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Obtiene una lista de productos por categoría.
     * @param category La categoría de productos.
     * @return La lista de productos si se obtienen correctamente, o un error si no se obtienen.*/
    suspend fun getProductsByCategory(category: String): Result<List<Product>> {
        return try {
            val products = apiService.getProductsByCategory(category)
            Result.success(products)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /** Obtiene una lista de productos en el carrito de compras.*/
    fun getCartItems(): Flow<List<CartItem>> {
        return cartDao.getAllCartItems().map { entities ->
            entities.map { entity ->
                CartItem(
                    product = Product(
                        id = entity.productId,
                        title = entity.title,
                        price = entity.price,
                        description = "",
                        category = "",
                        image = entity.image,
                        rating = Rating(0.0, 0)
                    ),
                    quantity = entity.quantity
                )
            }
        }
    }

    /** Agrega un producto al carrito de compras.
     * @param product El producto a agregar.
     * @param quantity La cantidad a agregar.
     * */
    suspend fun addToCart(product: Product, quantity: Int = 1) {
        val existingItem = cartDao.getCartItemByProductId(product.id)
        if (existingItem != null) {
            cartDao.updateCartItem(existingItem.copy(quantity = existingItem.quantity + quantity))
        } else {
            cartDao.insertCartItem(
                CartItemEntity(
                    productId = product.id,
                    title = product.title,
                    price = product.price,
                    image = product.image,
                    quantity = quantity
                )
            )
        }
    }

    /** Actualiza la cantidad de un producto en el carrito de compras.
     * @param productId El ID del producto.
     * @param quantity La nueva cantidad.
     * */
    suspend fun updateCartItemQuantity(productId: Int, quantity: Int) {
        val item = cartDao.getCartItemByProductId(productId)
        item?.let {
            if (quantity > 0) {
                cartDao.updateCartItem(it.copy(quantity = quantity))
            } else {
                cartDao.deleteCartItem(it)
            }
        }
    }

    /** Elimina un producto del carrito de compras.
     * @param productId El ID del producto.
     * */
    suspend fun removeFromCart(productId: Int) {
        val item = cartDao.getCartItemByProductId(productId)
        item?.let { cartDao.deleteCartItem(it) }
    }

    /** Elimina todos los productos del carrito de compras.
     * */
    suspend fun clearCart() {
        cartDao.clearCart()
    }
}