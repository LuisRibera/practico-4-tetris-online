package com.example.tetrisduel.game

import org.junit.Assert.assertEquals
import org.junit.Test

class TablaAtaqueTest {

    @Test
    fun lineasBasuraSigueLaTabla() {
        assertEquals(0, TablaAtaque.lineasBasura(1))
        assertEquals(1, TablaAtaque.lineasBasura(2))
        assertEquals(2, TablaAtaque.lineasBasura(3))
        assertEquals(4, TablaAtaque.lineasBasura(4))
    }

    @Test
    fun lineasBasuraEnCasosLimite() {
        assertEquals(0, TablaAtaque.lineasBasura(0))
        assertEquals(0, TablaAtaque.lineasBasura(5))
    }

    @Test
    fun puntajeSegunLineas() {
        assertEquals(0, TablaAtaque.puntaje(0))
        assertEquals(100, TablaAtaque.puntaje(1))
        assertEquals(300, TablaAtaque.puntaje(2))
        assertEquals(500, TablaAtaque.puntaje(3))
        assertEquals(800, TablaAtaque.puntaje(4))
    }
}
