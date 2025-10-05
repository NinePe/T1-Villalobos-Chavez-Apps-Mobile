package com.example.villaloboschavezt1.model

data class EstadoDialogo(
    val mostrarDiaglogoNuevoLibro: Boolean = false,
    val mostrarConfirmacion: Boolean = false,
    val libroAEliminar: Libro? = null,
    val mostrarDialogoNuevoLibro: Boolean = false
)