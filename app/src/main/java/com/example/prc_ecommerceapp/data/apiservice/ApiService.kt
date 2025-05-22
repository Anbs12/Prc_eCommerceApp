package com.example.prc_ecommerceapp.data.apiservice

import com.example.prc_ecommerceapp.data.model.Product
import com.example.prc_ecommerceapp.data.model.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/** Interfaz que define las operaciones de la API. */
interface ApiService {
    /** Obtiene una lista de productos. */
    @GET("products")
    suspend fun getProducts(): List<Product>

    /** Obtiene un producto por su ID. */
    @GET("products/{id}")
    suspend fun getProduct(@Path("id") id: Int): Product

    /** Obtiene una lista de categorías de productos. */
    @GET("products/categories")
    suspend fun getCategories(): List<String>

    /** Obtiene una lista de productos por categoría. */
    @GET("products/category/{category}")
    suspend fun getProductsByCategory(@Path("category") category: String): List<Product>

    /** Inicia sesión con las credenciales proporcionadas. */
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): LoginResponse

    /** Registra un nuevo usuario. */
    @POST("users")
    suspend fun register(@Body user: User): User
}

/** Modelo de datos para la solicitud de inicio de sesión. */
data class LoginRequest(
    val username: String,
    val password: String
)

/** Modelo de datos para la respuesta de inicio de sesión. */
data class LoginResponse(
    val token: String
)