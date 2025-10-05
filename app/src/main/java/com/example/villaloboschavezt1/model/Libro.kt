package com.example.villaloboschavezt1.model

import java.util.*

data class Libro(
    val titulo: String,
    val precio: Double,
    val cantidad: Int,
    val categoria: CategoriaLibro
) {
    fun calcularSubtotal(): Double = precio * cantidad
}