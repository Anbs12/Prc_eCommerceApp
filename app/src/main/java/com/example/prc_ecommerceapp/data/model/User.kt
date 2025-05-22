package com.example.prc_ecommerceapp.data.model

/*
Con esta clase podemos registrar e iniciar sesion de usuarios.
Hay que construir login y registro de usuarios, ademas de las bd y demas.
 */

/** Modelo de datos para representar nuestro usuario. */
data class User(
    val id: Int,
    val email: String,
    val username: String,
    val password: String,
    val name: Name,
    val address: Address?=null,
    val phone: String
)

/** Modelo de datos para representar el nombre del usuario. */
data class Name(
    val firstname: String,
    val lastname: String
)

/** Modelo de datos para representar la dirección del usuario. (De ser necesaria a la hroa
 * de añadir dicha direccion*/
data class Address(
    val city: String,
    val street: String,
    val number: Int,
    val zipcode: String,
    val geolocation: Geolocation
)

/** Modelo de datos para representar la geolocalización del usuario. Podemos
 * obtener datos de que usuarois se han registrado en el globo terraqueo*/
data class Geolocation(
    val lat: String,
    val long: String
)
