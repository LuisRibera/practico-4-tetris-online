package com.example.tetrisduel.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tetrisduel.R
import com.example.tetrisduel.ui.theme.AcentoCian
import com.example.tetrisduel.ui.theme.FondoOscuro
import com.example.tetrisduel.ui.theme.SuperficieOscura
import com.example.tetrisduel.ui.theme.TextoClaro
import com.example.tetrisduel.ui.theme.TextoTenue

@Composable
fun ResultadoScreen(
    gano: Boolean,
    puntaje: Int,
    lineas: Int,
    duracion: Int,
    mensaje: String,
    onVolverASala: () -> Unit,
) {
    BackHandler {
        onVolverASala()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = FondoOscuro
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = SuperficieOscura
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = if (gano) {
                            stringResource(R.string.texto_ganaste)
                        } else {
                            stringResource(R.string.texto_perdiste)
                        },
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextoClaro,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = mensaje,
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextoTenue,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(
                            R.string.resumen_partida,
                            puntaje,
                            lineas,
                            duracion
                        ),
                        style = MaterialTheme.typography.bodyLarge,
                        color = TextoClaro,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = stringResource(R.string.texto_nueva_sala),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoTenue,
                        textAlign = TextAlign.Center
                    )
                    Button(
                        onClick = onVolverASala,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AcentoCian,
                            contentColor = FondoOscuro
                        )
                    ) {
                        Text(
                            text = stringResource(R.string.accion_volver_a_sala),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
