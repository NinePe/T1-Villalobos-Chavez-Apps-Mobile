package com.example.villaloboschavezt1.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.villaloboschavezt1.model.CategoriaLibro

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DialogoNuevoLibro(
    titulo: String,
    precioTexto: String,
    cantidadTexto: String,
    categoria: CategoriaLibro,
    onTituloChange: (String) -> Unit,
    onPrecioChange: (String) -> Unit,
    onCantidadChange: (String) -> Unit,
    onCategoriaChange: (CategoriaLibro) -> Unit,
    onDismiss: () -> Unit,
    onConfirmar: () -> Unit
) {
    // Validaciones simples
    val tituloInvalido = titulo.isBlank()
    val precioValido = precioTexto.toDoubleOrNull()?.let { it >= 0.0 } == true
    val cantidadValida = cantidadTexto.toIntOrNull()?.let { it > 0 } == true
    val hayErrores = tituloInvalido || !precioValido || !cantidadValida

    // Estado del dropdown
    var expanded by remember { mutableStateOf(false) }
    val opciones = remember { CategoriaLibro.values().toList() }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Nuevo Libro",
                style = MaterialTheme.typography.headlineSmall
            )
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Título
                OutlinedTextField(
                    value = titulo,
                    onValueChange = onTituloChange,
                    label = { Text("Título *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    isError = tituloInvalido,
                    supportingText = { if (tituloInvalido) Text("Campo obligatorio") }
                )

                // Precio
                OutlinedTextField(
                    value = precioTexto,
                    onValueChange = onPrecioChange,
                    label = { Text("Precio *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    isError = !precioValido,
                    supportingText = { if (!precioValido) Text("Ingresa un número válido") }
                )

                // Cantidad
                OutlinedTextField(
                    value = cantidadTexto,
                    onValueChange = onCantidadChange,
                    label = { Text("Cantidad *") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = !cantidadValida,
                    supportingText = { if (!cantidadValida) Text("Debe ser entero mayor a 0") }
                )

                // Categoría (Dropdown)
                Text(
                    text = "Categoría",
                    style = MaterialTheme.typography.labelLarge
                )

                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { expanded = !expanded },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        value = categoria.name, // cambia por un displayName si quieres
                        onValueChange = {},
                        label = { Text("Selecciona una categoría") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth()
                    )

                    ExposedDropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        opciones.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.name) },
                                onClick = {
                                    onCategoriaChange(opcion)
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirmar,
                enabled = !hayErrores
            ) { Text("Agregar") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
