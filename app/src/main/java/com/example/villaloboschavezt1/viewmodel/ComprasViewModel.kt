package com.example.villaloboschavezt1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.villaloboschavezt1.model.CategoriaLibro
import com.example.villaloboschavezt1.model.Libro
import com.example.villaloboschavezt1.model.EstadoDialogo
import com.example.villaloboschavezt1.model.Notificacion
import com.example.villaloboschavezt1.model.TipoNotificacion

class ComprasViewModel : ViewModel() {

    var libros by mutableStateOf(obtenerLibrosIniciales())
        private set
    var estadoDialogo by mutableStateOf(EstadoDialogo())
        private set
    var notificacion by mutableStateOf<Notificacion?>(null)
        private set
    var tituloNuevoLibro by mutableStateOf("")
        private set
    var cantidadNuevoLibro by mutableStateOf("")
        private set
    var precioNuevoLibro by mutableStateOf("")
        private set
    var categoriaNuevoLibro by mutableStateOf(CategoriaLibro.INFANTIL)
        private set

    // Funciones para manejar estado de datos
    fun actualizarTitulo(nuevoTitulo: String) {
        tituloNuevoLibro = nuevoTitulo
    }
    fun actualizarCantidad(nuevaCantidad: String) {
        cantidadNuevoLibro = nuevaCantidad
    }
    fun actualizarPrecio(nuevoPrecio: String) {
        precioNuevoLibro = nuevoPrecio
    }
    fun actualizarCategoria(nuevaCategoria: CategoriaLibro) {
        categoriaNuevoLibro = nuevaCategoria
    }

    fun mostrarDialogoNuevoLibro(){
        estadoDialogo = estadoDialogo.copy(mostrarDialogoNuevoLibro = true)
        limpiarFormulario()
    }

    fun ocultarDialogoNuevoLibro(){
        estadoDialogo = estadoDialogo.copy(mostrarDialogoNuevoLibro = false)
        limpiarFormulario()
    }

    // Manejo de acciones CRUD

    fun agregarLibro() {
        if(tituloNuevoLibro.isBlank()){
            mostrarNotificacion("El Titulo del Libro es obligatorio", TipoNotificacion.ERROR)
            return
        }
        val nuevoLibro = Libro(
            id = libros.size + 1,
            titulo = tituloNuevoLibro.trim(),
            precio = precioNuevoLibro.toDouble(),
            cantidad = cantidadNuevoLibro.toInt(),
            categoria = categoriaNuevoLibro
        )
        libros = libros + nuevoLibro
        ocultarDialogoNuevoLibro()
        mostrarNotificacion("Libro agregado correctamente", TipoNotificacion.EXITO)
    }

    fun mostrarNotificacion(mensaje: String, tipo: TipoNotificacion){
        notificacion = Notificacion(mensaje, tipo,true)
    }

    fun ocultarNotificacion(){
        notificacion = notificacion?.copy(visible = false)
    }

    private fun limpiarFormulario() {
        tituloNuevoLibro = ""
        cantidadNuevoLibro = ""
        precioNuevoLibro = ""
        categoriaNuevoLibro = CategoriaLibro.INFANTIL
    }

    private fun obtenerLibrosIniciales(): List<Libro> = listOf(
        Libro(1, "El Principito", 59.9, cantidad = 1, categoria = CategoriaLibro.INFANTIL),
        Libro(2, "Cien años de soledad", 59.9, cantidad = 1, categoria = CategoriaLibro.DRAMA)
    )

    fun eliminarLibro() {
        estadoDialogo.libroAEliminar?.let { libro ->
            libros = libros.filter { it.id != libro.id }
            estadoDialogo = estadoDialogo.copy(mostrarConfirmacion = false)
            mostrarNotificacion("Libro eliminado correctamente", TipoNotificacion.EXITO)
        }
    }

    fun alternarCompletadaLibro(libro: Libro) {
        libros = libros.map {
            if (it.id == libro.id) {
                it.copy(completada = !it.completada)
            } else {
                it
            }
        }
    }

    // Confirmaciones

    fun mostrarDialogoConfirmacion(libro: Libro) {
        estadoDialogo = estadoDialogo.copy(
            mostrarConfirmacion = true,
            libroAEliminar = libro
        )
    }

    fun ocultarDialogoConfirmacion() {
        estadoDialogo = estadoDialogo.copy(
            mostrarConfirmacion = false,
            libroAEliminar = null
        )
    }
}


