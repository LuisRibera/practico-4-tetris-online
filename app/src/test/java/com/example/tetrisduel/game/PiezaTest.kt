package com.example.tetrisduel.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PiezaTest {

    @Test
    fun haySieteTiposDePieza() {
        assertEquals(7, TipoPieza.entries.size)
    }

    @Test
    fun cadaTipoTieneColorUnicoEntreUnoYSiete() {
        val colores = TipoPieza.entries.map { it.color }
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7), colores.sorted())
    }

    @Test
    fun celdasReflejanLaPosicion() {
        val pieza = Pieza(TipoPieza.O, fila = 5, columna = 3, rotacion = 0)
        assertTrue(pieza.celdas.contains(5 to 3))
        assertTrue(pieza.celdas.contains(5 to 4))
        assertTrue(pieza.celdas.contains(6 to 3))
        assertTrue(pieza.celdas.contains(6 to 4))
    }

    @Test
    fun movidaDesplazaLaPieza() {
        val pieza = Pieza(TipoPieza.T, fila = 0, columna = 0, rotacion = 0)
        val movida = pieza.movida(2, 3)
        assertEquals(2, movida.fila)
        assertEquals(3, movida.columna)
    }

    @Test
    fun rotadaAvanzaYVuelveAlInicio() {
        val pieza = Pieza(TipoPieza.O, fila = 0, columna = 0, rotacion = 0)
        assertEquals(0, pieza.rotada().rotacion)

        val t = Pieza(TipoPieza.T, fila = 0, columna = 0, rotacion = 3)
        assertEquals(0, t.rotada().rotacion)
    }
}
