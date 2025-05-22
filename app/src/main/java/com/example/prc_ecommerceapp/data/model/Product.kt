package com.example.prc_ecommerceapp.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/** Modelo de datos para representar un producto. */
@Parcelize
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
) : Parcelable

/** Modelo de datos para representar una calificación. */
@Parcelize
data class Rating(
    val rate: Double,
    val count: Int
) : Parcelable
