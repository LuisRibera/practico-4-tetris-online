package com.example.tetrisduel.ui.screens.components

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import com.example.tetrisduel.game.MotorTetris
import com.example.tetrisduel.game.Pieza
import com.example.tetrisduel.game.Tablero
import com.example.tetrisduel.game.TipoPieza
import com.example.tetrisduel.ui.theme.FondoOscuro
import com.example.tetrisduel.ui.theme.SuperficieOscura

@Composable
fun TableroCanvas(
    tablero: Tablero,
    piezaActual: Pieza?,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val anchoCelda = size.width / MotorTetris.COLUMNAS
        val altoCelda = size.height / MotorTetris.FILAS

        drawRect(color = FondoOscuro)

        for (fila in 0 until MotorTetris.FILAS) {
            for (columna in 0 until MotorTetris.COLUMNAS) {
                val color = colorCelda(tablero[fila][columna])
                if (color != Color.Transparent) {
                    drawRect(
                        color = color,
                        topLeft = Offset(columna * anchoCelda, fila * altoCelda),
                        size = Size(anchoCelda, altoCelda)
                    )
                }
            }
        }

        piezaActual?.celdas?.forEach { (fila, columna) ->
            if (fila in 0 until MotorTetris.FILAS && columna in 0 until MotorTetris.COLUMNAS) {
                drawRect(
                    color = colorCelda(piezaActual.tipo.color),
                    topLeft = Offset(columna * anchoCelda, fila * altoCelda),
                    size = Size(anchoCelda, altoCelda)
                )
            }
        }

        for (columna in 0..MotorTetris.COLUMNAS) {
            val x = columna * anchoCelda
            drawLine(
                color = SuperficieOscura,
                start = Offset(x, 0f),
                end = Offset(x, size.height)
            )
        }

        for (fila in 0..MotorTetris.FILAS) {
            val y = fila * altoCelda
            drawLine(
                color = SuperficieOscura,
                start = Offset(0f, y),
                end = Offset(size.width, y)
            )
        }
    }
}

@Composable
fun SiguientePiezaCanvas(
    tipoPieza: TipoPieza,
    modifier: Modifier = Modifier
) {
    val pieza = Pieza(tipoPieza, fila = 0, columna = 0, rotacion = 0)
    val filas = pieza.celdas.map { it.first }
    val columnas = pieza.celdas.map { it.second }
    val filaMin = filas.minOrNull() ?: 0
    val filaMax = filas.maxOrNull() ?: 0
    val columnaMin = columnas.minOrNull() ?: 0
    val columnaMax = columnas.maxOrNull() ?: 0
    val alto = (filaMax - filaMin + 1).coerceAtLeast(4)
    val ancho = (columnaMax - columnaMin + 1).coerceAtLeast(4)

    Canvas(modifier = modifier) {
        val anchoCelda = size.width / ancho
        val altoCelda = size.height / alto

        drawRect(color = FondoOscuro)

        pieza.celdas.forEach { (fila, columna) ->
            val filaAjustada = fila - filaMin + (alto - (filaMax - filaMin + 1)) / 2f
            val columnaAjustada = columna - columnaMin + (ancho - (columnaMax - columnaMin + 1)) / 2f
            drawRect(
                color = colorCelda(tipoPieza.color),
                topLeft = Offset(columnaAjustada * anchoCelda, filaAjustada * altoCelda),
                size = Size(anchoCelda, altoCelda)
            )
        }

        for (columna in 0..ancho) {
            val x = columna * anchoCelda
            drawLine(
                color = SuperficieOscura,
                start = Offset(x, 0f),
                end = Offset(x, size.height)
            )
        }

        for (fila in 0..alto) {
            val y = fila * altoCelda
            drawLine(
                color = SuperficieOscura,
                start = Offset(0f, y),
                end = Offset(size.width, y)
            )
        }
    }
}

private fun colorCelda(valor: Int): Color = when (valor) {
    1 -> Color(0xFF00E5FF)
    2 -> Color(0xFFFFD54F)
    3 -> Color(0xFFB388FF)
    4 -> Color(0xFF69F0AE)
    5 -> Color(0xFFFF6E6E)
    6 -> Color(0xFF82B1FF)
    7 -> Color(0xFFFFAB40)
    MotorTetris.COLOR_BASURA -> Color(0xFF5C637A)
    else -> Color.Transparent
}
