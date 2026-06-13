package com.example.tetrisduel.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.tetrisduel.R
import com.example.tetrisduel.ui.screens.components.SiguientePiezaCanvas
import com.example.tetrisduel.ui.screens.components.TableroCanvas
import com.example.tetrisduel.ui.theme.AcentoCian
import com.example.tetrisduel.ui.theme.AcentoMagenta
import com.example.tetrisduel.ui.theme.FondoOscuro
import com.example.tetrisduel.ui.theme.SuperficieOscura
import com.example.tetrisduel.ui.theme.TextoClaro
import com.example.tetrisduel.ui.theme.TextoTenue
import com.example.tetrisduel.ui.viewmodels.JuegoViewModel

@Composable
fun JuegoScreen(
    codigoSala: String,
    viewModel: JuegoViewModel = hiltViewModel()
) {
    val estado by viewModel.estado.collectAsState()

    LaunchedEffect(codigoSala) {
        viewModel.iniciar(codigoSala)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoOscuro)
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(R.string.titulo_juego),
                style = MaterialTheme.typography.headlineMedium,
                color = TextoClaro
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                TarjetaDato(
                    titulo = stringResource(R.string.etiqueta_puntaje),
                    valor = estado.puntaje.toString(),
                    modifier = Modifier.weight(1f)
                )
                TarjetaDato(
                    titulo = stringResource(R.string.etiqueta_lineas),
                    valor = estado.lineas.toString(),
                    modifier = Modifier.weight(1f)
                )
                TarjetaDato(
                    titulo = stringResource(R.string.etiqueta_sala),
                    valor = codigoSala,
                    modifier = Modifier.weight(1f)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .border(1.dp, SuperficieOscura, RoundedCornerShape(18.dp))
                        .background(SuperficieOscura, RoundedCornerShape(18.dp))
                        .padding(8.dp)
                ) {
                    TableroCanvas(
                        tablero = estado.tablero,
                        piezaActual = estado.piezaActual,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.5f)
                    )
                }

                Column(
                    modifier = Modifier.width(118.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    TarjetaEstado(
                        titulo = stringResource(R.string.etiqueta_conexion),
                        valor = if (estado.conectado) {
                            stringResource(R.string.estado_conectado)
                        } else {
                            stringResource(R.string.estado_desconectado)
                        },
                        color = if (estado.conectado) AcentoCian else AcentoMagenta
                    )

                    TarjetaEstado(
                        titulo = stringResource(R.string.etiqueta_oponente),
                        valor = if (estado.oponenteConectado) {
                            stringResource(R.string.estado_listo)
                        } else {
                            stringResource(R.string.estado_fuera)
                        },
                        color = if (estado.oponenteConectado) AcentoCian else AcentoMagenta
                    )

                    Card {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(SuperficieOscura)
                                .padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = stringResource(R.string.etiqueta_siguiente),
                                style = MaterialTheme.typography.labelLarge,
                                color = TextoTenue
                            )
                            SiguientePiezaCanvas(
                                tipoPieza = estado.siguiente,
                                modifier = Modifier.size(86.dp)
                            )
                        }
                    }
                }
            }

            ControlesJuego(
                onIzquierda = viewModel::moverIzquierda,
                onRotar = viewModel::rotar,
                onDerecha = viewModel::moverDerecha,
                onBajar = viewModel::bajar,
                onCaida = viewModel::caidaInstantanea,
                habilitado = !estado.terminado
            )
        }

        if (estado.mostrarLucky37) {
            Surface(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 72.dp),
                shape = RoundedCornerShape(999.dp),
                color = AcentoMagenta
            ) {
                Text(
                    text = stringResource(R.string.aviso_lucky_37),
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    color = TextoClaro,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        if (estado.terminado) {
            Surface(
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color(0xEE1B2236)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = if (estado.gano) {
                            stringResource(R.string.texto_ganaste)
                        } else {
                            stringResource(R.string.texto_perdiste)
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        color = TextoClaro
                    )
                    estado.mensajeFinal?.let { mensaje ->
                        Text(
                            text = mensaje,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextoTenue,
                            textAlign = TextAlign.Center
                        )
                    }
                    Text(
                        text = stringResource(
                            R.string.resumen_partida,
                            estado.puntaje,
                            estado.lineas,
                            estado.duracionSegundos
                        ),
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextoClaro,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

@Composable
private fun TarjetaDato(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SuperficieOscura)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelLarge,
                color = TextoTenue
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.titleLarge,
                color = TextoClaro,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun TarjetaEstado(
    titulo: String,
    valor: String,
    color: Color
) {
    Card {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(SuperficieOscura)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelLarge,
                color = TextoTenue
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium,
                color = color,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun ControlesJuego(
    onIzquierda: () -> Unit,
    onRotar: () -> Unit,
    onDerecha: () -> Unit,
    onBajar: () -> Unit,
    onCaida: () -> Unit,
    habilitado: Boolean
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BotonControl(
                texto = stringResource(R.string.accion_izquierda),
                onClick = onIzquierda,
                habilitado = habilitado,
                modifier = Modifier.weight(1f)
            )
            BotonControl(
                texto = stringResource(R.string.accion_rotar),
                onClick = onRotar,
                habilitado = habilitado,
                modifier = Modifier.weight(1f)
            )
            BotonControl(
                texto = stringResource(R.string.accion_derecha),
                onClick = onDerecha,
                habilitado = habilitado,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BotonControl(
                texto = stringResource(R.string.accion_bajar),
                onClick = onBajar,
                habilitado = habilitado,
                modifier = Modifier.weight(1f)
            )
            BotonControl(
                texto = stringResource(R.string.accion_caida),
                onClick = onCaida,
                habilitado = habilitado,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BotonControl(
    texto: String,
    onClick: () -> Unit,
    habilitado: Boolean,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        enabled = habilitado,
        colors = ButtonDefaults.buttonColors(
            containerColor = AcentoCian,
            contentColor = FondoOscuro,
            disabledContainerColor = SuperficieOscura,
            disabledContentColor = TextoTenue
        )
    ) {
        Text(
            text = texto,
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold
        )
    }
}
