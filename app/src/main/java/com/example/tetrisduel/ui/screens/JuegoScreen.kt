package com.example.tetrisduel.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.tetrisduel.R

@Composable
fun JuegoScreen(codigoSala: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.titulo_juego),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = "Sala: $codigoSala",
            style = MaterialTheme.typography.bodyLarge
        )
        Text(
            text = stringResource(R.string.texto_juego_placeholder),
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
