package com.example.tetrisduel.game

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class MotorTetrisTest {

    @Test
    fun tableroVacioTieneDimensionesCorrectas() {
        val tablero = MotorTetris.tableroVacio()
        assertEquals(MotorTetris.FILAS, tablero.size)
        assertEquals(MotorTetris.COLUMNAS, tablero[0].size)
        assertTrue(tablero.all { fila -> fila.all { it == 0 } })
    }

    @Test
    fun posicionInicialEsValida() {
        val tablero = MotorTetris.tableroVacio()
        val pieza = MotorTetris.posicionInicial(TipoPieza.T)
        assertTrue(MotorTetris.posicionValida(tablero, pieza))
    }

    @Test
    fun posicionInvalidaFueraDelBorde() {
        val tablero = MotorTetris.tableroVacio()
        val pieza = MotorTetris.posicionInicial(TipoPieza.O).movida(0, -5)
        assertFalse(MotorTetris.posicionValida(tablero, pieza))
    }

    @Test
    fun posicionInvalidaSobreCeldaOcupada() {
        val tablero = MotorTetris.tableroVacio()
        tablero[0][3] = MotorTetris.COLOR_BASURA
        val pieza = MotorTetris.posicionInicial(TipoPieza.O)
        assertFalse(MotorTetris.posicionValida(tablero, pieza))
    }

    @Test
    fun fijarColocaLosColoresDeLaPieza() {
        val tablero = MotorTetris.tableroVacio()
        val pieza = MotorTetris.posicionInicial(TipoPieza.O)
        val resultado = MotorTetris.fijar(tablero, pieza)
        for ((fila, columna) in pieza.celdas) {
            assertEquals(TipoPieza.O.color, resultado[fila][columna])
        }
    }

    @Test
    fun limpiarLineaCompletaYBajarContenido() {
        val tablero = MotorTetris.tableroVacio()
        for (columna in 0 until MotorTetris.COLUMNAS) {
            tablero[19][columna] = TipoPieza.I.color
        }
        tablero[18][0] = TipoPieza.T.color

        val (resultado, eliminadas) = MotorTetris.limpiarLineas(tablero)

        assertEquals(1, eliminadas)
        assertEquals(TipoPieza.T.color, resultado[19][0])
        assertEquals(0, resultado[19][1])
    }

    @Test
    fun agregarBasuraEmpujaHaciaArribaYDejaUnHueco() {
        val tablero = MotorTetris.tableroVacio()
        tablero[19][5] = TipoPieza.L.color

        val resultado = MotorTetris.agregarBasura(tablero, 1)

        assertEquals(TipoPieza.L.color, resultado[18][5])
        val huecos = (0 until MotorTetris.COLUMNAS).count { resultado[19][it] == 0 }
        assertEquals(1, huecos)
    }

    @Test
    fun caidaInstantaneaLlegaAlFondo() {
        val tablero = MotorTetris.tableroVacio()
        val pieza = MotorTetris.posicionInicial(TipoPieza.O)
        val caida = MotorTetris.caidaInstantanea(tablero, pieza)
        val abajo = caida.movida(1, 0)
        assertFalse(MotorTetris.posicionValida(tablero, abajo))
    }
}
