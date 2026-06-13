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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tetrisduel.R
import com.example.tetrisduel.ui.screens.components.SiguientePiezaCanvas
import com.example.tetrisduel.ui.screens.components.TableroCanvas
import com.example.tetrisduel.ui.states.EstadoJuego
import com.example.tetrisduel.ui.theme.AcentoCian
import com.example.tetrisduel.ui.theme.AcentoMagenta
import com.example.tetrisduel.ui.theme.FondoOscuro
import com.example.tetrisduel.ui.theme.SuperficieOscura
import com.example.tetrisduel.ui.theme.TextoClaro
import com.example.tetrisduel.ui.theme.TextoTenue

@Composable
fun JuegoScreen(
    codigoSala: String,
    estado: EstadoJuego,
    onIniciar: (String) -> Unit,
    onMoverIzquierda: () -> Unit,
    onRotar: () -> Unit,
    onMoverDerecha: () -> Unit,
    onBajar: () -> Unit,
    onCaidaInstantanea: () -> Unit,
    onIrAResultado: (Boolean, Int, Int, Int, String) -> Unit,
) {
    LaunchedEffect(codigoSala) {
        onIniciar(codigoSala)
    }

    LaunchedEffect(estado.terminado) {
        if (estado.terminado) {
            onIrAResultado(
                estado.gano,
                estado.puntaje,
                estado.lineas,
                estado.duracionSegundos,
                estado.mensajeFinal.orEmpty()
            )
        }
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
                onIzquierda = onMoverIzquierda,
                onRotar = onRotar,
                onDerecha = onMoverDerecha,
                onBajar = onBajar,
                onCaida = onCaidaInstantanea,
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
                texto = stringResource(R.string.icono_izquierda),
                onClick = onIzquierda,
                habilitado = habilitado,
                esIcono = true,
                modifier = Modifier.weight(1f)
            )
            BotonControl(
                texto = stringResource(R.string.icono_rotar),
                onClick = onRotar,
                habilitado = habilitado,
                esIcono = true,
                modifier = Modifier.weight(1f)
            )
            BotonControl(
                texto = stringResource(R.string.icono_derecha),
                onClick = onDerecha,
                habilitado = habilitado,
                esIcono = true,
                modifier = Modifier.weight(1f)
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            BotonControl(
                texto = stringResource(R.string.icono_bajar),
                onClick = onBajar,
                habilitado = habilitado,
                esIcono = true,
                modifier = Modifier.weight(1f)
            )
            BotonControl(
                texto = stringResource(R.string.icono_soltar),
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
    esIcono: Boolean = false,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
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
            fontWeight = FontWeight.Bold,
            style = if (esIcono) {
                MaterialTheme.typography.titleLarge
            } else {
                MaterialTheme.typography.labelLarge
            }
        )
    }
}
