package com.example.villaloboschavezt1.model

import java.util.*

data class Libro(
    val id: Int,
    val titulo: String,
    val precio: Double,
    val cantidad: Int,
    val categoria: CategoriaLibro,
    val completada: Boolean = false
) {
    fun calcularSubtotal(): Double = precio * cantidad
}