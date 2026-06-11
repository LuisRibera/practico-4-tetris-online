package com.example.tetrisduel.game

object TablaAtaque {
    fun lineasBasura(lineasEliminadas: Int): Int = when (lineasEliminadas) {
        1 -> 0
        2 -> 1
        3 -> 2
        4 -> 4
        else -> 0
    }

    fun puntaje(lineasEliminadas: Int): Int = when (lineasEliminadas) {
        1 -> 100
        2 -> 300
        3 -> 500
        4 -> 800
        else -> 0
    }
}
