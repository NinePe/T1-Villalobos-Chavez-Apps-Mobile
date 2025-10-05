package com.example.villaloboschavezt1.model

data class Notificacion(
    val mensaje: String,
    val tipo: TipoNotificacion,
    val visible: Boolean = false
)