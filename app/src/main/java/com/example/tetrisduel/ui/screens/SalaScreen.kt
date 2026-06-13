package com.example.tetrisduel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tetrisduel.R
import com.example.tetrisduel.ui.states.EstadoSala

@Composable
fun SalaScreen(
    estado: EstadoSala,
    onCambiarUrl: (String) -> Unit,
    onCambiarCodigo: (String) -> Unit,
    onConectar: () -> Unit,
    onCrearSala: () -> Unit,
    onUnirseSala: () -> Unit,
    onIrAJuego: (String) -> Unit,
) {
    LaunchedEffect(estado.partidaIniciada, estado.codigoSala) {
        val codigoSala = estado.codigoSala
        if (estado.partidaIniciada && !codigoSala.isNullOrBlank()) {
            onIrAJuego(codigoSala)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_sala),
            style = MaterialTheme.typography.headlineMedium
        )

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = estado.url,
                    onValueChange = onCambiarUrl,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.etiqueta_url_servidor)) },
                    singleLine = true
                )

                Button(
                    onClick = onConectar,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !estado.conectando
                ) {
                    Text(stringResource(R.string.accion_conectar))
                }

                Text(
                    text = when {
                        estado.conectando -> stringResource(R.string.estado_conectando)
                        estado.conectado -> stringResource(R.string.estado_conectado)
                        else -> stringResource(R.string.estado_desconectado)
                    },
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onCrearSala,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !estado.conectando
                ) {
                    Text(stringResource(R.string.accion_crear_sala))
                }

                OutlinedTextField(
                    value = estado.codigoIngresado,
                    onValueChange = onCambiarCodigo,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(stringResource(R.string.etiqueta_codigo_sala)) },
                    singleLine = true
                )

                Button(
                    onClick = onUnirseSala,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !estado.conectando
                ) {
                    Text(stringResource(R.string.accion_unirse_sala))
                }

                estado.codigoSala?.let { codigo ->
                    Text(
                        text = "Sala actual: $codigo",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                if (estado.esperandoOponente) {
                    Text(
                        text = stringResource(R.string.estado_esperando),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        estado.mensaje?.takeIf { it.isNotBlank() }?.let { mensaje ->
            Text(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
