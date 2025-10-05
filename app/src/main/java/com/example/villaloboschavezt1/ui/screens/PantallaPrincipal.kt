package com.example.villaloboschavezt1.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.villaloboschavezt1.ui.components.BarraNotificacion
import com.example.villaloboschavezt1.ui.components.DialogoConfirmacion
import com.example.villaloboschavezt1.ui.components.DialogoNuevoLibro
import com.example.villaloboschavezt1.ui.components.TarjetaLibro
import com.example.villaloboschavezt1.viewmodel.ComprasViewModel
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPrincipal(
    viewModel: ComprasViewModel = viewModel()
){
    Scaffold (
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Compra de Libros",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { viewModel.mostrarDialogoNuevoLibro() },
                containerColor = MaterialTheme.colorScheme.primary,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar Libro"
                )
            }
        }
    ){ paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ){
            ContenidoPrincipal(viewModel)

            // Snackbar/Barra de notificación centrada abajo
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.BottomCenter
            ){
                BarraNotificacion(
                    notificacion = viewModel.notificacion,
                    onDismiss = { viewModel.ocultarNotificacion() }
                )
            }
        }

        // Diálogo "Nuevo libro"
        if (viewModel.estadoDialogo.mostrarDialogoNuevoLibro){
            DialogoNuevoLibro(
                titulo = viewModel.tituloNuevoLibro,
                precioTexto = viewModel.precioNuevoLibro,
                cantidadTexto = viewModel.cantidadNuevoLibro,
                categoria = viewModel.categoriaNuevoLibro,
                onTituloChange = { viewModel.actualizarTitulo(it) },
                onPrecioChange = { viewModel.actualizarPrecio(it) },
                onCantidadChange = { viewModel.actualizarCantidad(it) },
                onCategoriaChange = { viewModel.actualizarCategoria(it) },
                onDismiss = { viewModel.ocultarDialogoNuevoLibro() },
                onConfirmar = { viewModel.agregarLibro() }
            )
        }
        if (viewModel.estadoDialogo.mostrarConfirmacion){
            viewModel.estadoDialogo.libroAEliminar?.let { libro ->
                DialogoConfirmacion(
                    libro = libro,
                    onConfirmar = { viewModel.eliminarLibro() },
                    onDismiss = { viewModel.ocultarDialogoConfirmacion() }
                )
            }
        }
    }
}

@Composable
fun ContenidoPrincipal(viewModel: ComprasViewModel) {
    if (viewModel.libros.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ){
                Text(
                    text = "No hay libros en la lista",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Presiona el botón flotante para agregar uno",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }
        }
    } else {
        Column(modifier = Modifier.fillMaxSize()) {
            // Lista ocupando el espacio disponible
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(items = viewModel.libros, key = { it.id }) { libro ->
                    TarjetaLibro(
                        libro = libro,
                        onCompletadaChange = { viewModel.alternarCompletadaLibro(libro) },
                        onEliminar = { viewModel.mostrarDialogoConfirmacion(libro) }
                    )
                }
            }

            // 🔻 Resumen compacto al final (no es bottomBar)
            SeccionResumenCompra(
                viewModel = viewModel,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 80.dp, top = 8.dp, bottom = 12.dp) // deja libre el FAB
            )
        }
    }
}
@Composable
private fun SeccionResumenCompra(
    viewModel: ComprasViewModel,
    modifier: Modifier = Modifier
) {
    val totalLibros = viewModel.libros.sumOf { it.cantidad }
    val subtotal = viewModel.libros.sumOf { it.precio * it.cantidad }

    val descuentoPct = when {
        totalLibros >= 11 -> 20
        totalLibros in 6..10 -> 15
        totalLibros in 3..5 -> 10
        totalLibros in 1..2 -> 0
        else -> 0
    }
    val descuento = subtotal * (descuentoPct / 100.0)
    val total = subtotal - descuento

    val labelStyle = MaterialTheme.typography.labelLarge.copy(
        color = MaterialTheme.colorScheme.onSurfaceVariant
    )
    val valueStyle = MaterialTheme.typography.labelLarge
    val totalStyle = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(2.dp) // compacto
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Subtotal", style = labelStyle)
            Text(precio(subtotal), style = valueStyle)
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Descuento", style = labelStyle)
            Text("- " + precio(descuento), style = valueStyle.copy(color = MaterialTheme.colorScheme.primary))
        }
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Total", style = totalStyle)
            Text(precio(total), style = totalStyle)
        }
    }
}

private fun precio(valor: Double): String =
    "S/ " + String.format(java.util.Locale.US, "%,.2f", valor)

