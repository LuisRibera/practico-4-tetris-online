package com.example.tetrisduel.ui.states

import com.example.tetrisduel.game.MotorTetris
import com.example.tetrisduel.game.Pieza
import com.example.tetrisduel.game.Tablero
import com.example.tetrisduel.game.TipoPieza

data class EstadoJuego(
    val tablero: Tablero = MotorTetris.tableroVacio(),
    val piezaActual: Pieza? = null,
    val siguiente: TipoPieza = TipoPieza.aleatoria(),
    val puntaje: Int = 0,
    val lineas: Int = 0,
    val piezasColocadas: Int = 0,
    val conectado: Boolean = true,
    val oponenteConectado: Boolean = true,
    val terminado: Boolean = false,
    val gano: Boolean = false,
    val duracionSegundos: Int = 0,
    val mostrarLucky37: Boolean = false
)
