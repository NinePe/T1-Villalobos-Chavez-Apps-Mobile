package com.example.villaloboschavezt1.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.villaloboschavezt1.model.Notificacion
import com.example.villaloboschavezt1.model.TipoNotificacion
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BarraNotificacion(
    notificacion: Notificacion?,
    onDismiss: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(notificacion) {
        notificacion?.let {
            if (it.visible) {
                // autocierre a los 2s
                val autoDismiss = launch {
                    delay(2000)
                    snackbarHostState.currentSnackbarData?.dismiss()
                }
                snackbarHostState.showSnackbar(
                    message = it.mensaje,
                    duration = SnackbarDuration.Indefinite
                )
                autoDismiss.cancel()
                onDismiss()
            }
        }
    }

    SnackbarHost(
        hostState = snackbarHostState,
        snackbar = { data ->
            Snackbar(
                modifier = Modifier.padding(14.dp),
                action = {
                    IconButton(onClick = { data.dismiss() }) {
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "Cerrar",
                            tint = Color.White
                        )
                    }
                }
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = obtenerIconoNotificacion(notificacion?.tipo),
                        contentDescription = "Icono de notificación",
                        tint = Color.White
                    )
                    Text(text = data.visuals.message)
                }
            }
        }
    )
}

private fun obtenerIconoNotificacion(tipo: TipoNotificacion?) = when (tipo) {
    TipoNotificacion.EXITO -> Icons.Filled.Check
    TipoNotificacion.ADVERTENCIA -> Icons.Filled.Warning
    TipoNotificacion.ERROR -> Icons.Filled.Error
    else -> Icons.Filled.Info
}

private fun obtenerColorNotificacion(tipo: TipoNotificacion?) = when (tipo) {
    TipoNotificacion.EXITO -> Color(0xFF2E7D32)      // verde
    TipoNotificacion.ADVERTENCIA -> Color.Yellow     // amarillo (texto negro)
    TipoNotificacion.ERROR -> Color(0xFFC62828)      // rojo
    else -> Color(0xFF323232)                        // gris
}
