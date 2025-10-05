package com.example.villaloboschavezt1.model

import java.util.*

class CompraLibros {
    var subtotal: Double = 0.0
        private set
    var totalLibros: Int = 0
        private set
    var porcentajeDesc: Double = 0.0   // 0.10, 0.15, 0.20
        private set
    var descuentoSoles: Double = 0.0
        private set
    var totalFinal: Double = 0.0
        private set

    fun calcular(items: List<Libro>) {
        subtotal = items.sumOf { it.calcularSubtotal() }
        totalLibros = items.sumOf { it.cantidad }
        porcentajeDesc = when {
            totalLibros in 1..2  -> 0.0
            totalLibros in 3..5  -> 0.10
            totalLibros in 6..10 -> 0.15
            totalLibros >= 11    -> 0.20
            else -> 0.0
        }
        descuentoSoles = subtotal * porcentajeDesc
        totalFinal = subtotal - descuentoSoles
    }
}