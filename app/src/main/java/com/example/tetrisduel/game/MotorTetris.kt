package com.example.tetrisduel.game

typealias Tablero = Array<IntArray>

object MotorTetris {
    const val COLUMNAS = 10
    const val FILAS = 20
    const val COLOR_BASURA = 8

    fun tableroVacio(): Tablero = Array(FILAS) { IntArray(COLUMNAS) }

    fun clonar(tablero: Tablero): Tablero = Array(tablero.size) { tablero[it].clone() }

    fun posicionInicial(tipo: TipoPieza): Pieza =
        Pieza(tipo = tipo, fila = 0, columna = COLUMNAS / 2 - 2, rotacion = 0)

    fun posicionValida(tablero: Tablero, pieza: Pieza): Boolean {
        for ((fila, columna) in pieza.celdas) {
            if (columna < 0 || columna >= COLUMNAS) return false
            if (fila >= FILAS) return false
            if (fila >= 0 && tablero[fila][columna] != 0) return false
        }
        return true
    }

    fun fijar(tablero: Tablero, pieza: Pieza): Tablero {
        val nuevo = clonar(tablero)
        for ((fila, columna) in pieza.celdas) {
            if (fila in 0 until FILAS && columna in 0 until COLUMNAS) {
                nuevo[fila][columna] = pieza.tipo.color
            }
        }
        return nuevo
    }

    fun limpiarLineas(tablero: Tablero): Pair<Tablero, Int> {
        val filasRestantes = tablero.filter { fila -> fila.any { it == 0 } }
        val eliminadas = FILAS - filasRestantes.size
        if (eliminadas == 0) return tablero to 0
        val nuevo = tableroVacio()
        var destino = FILAS - 1
        for (i in filasRestantes.indices.reversed()) {
            nuevo[destino] = filasRestantes[i].clone()
            destino--
        }
        return nuevo to eliminadas
    }

    fun agregarBasura(tablero: Tablero, cantidad: Int): Tablero {
        if (cantidad <= 0) return tablero
        val nuevo = tableroVacio()
        for (fila in cantidad until FILAS) {
            nuevo[fila - cantidad] = tablero[fila].clone()
        }
        for (i in 0 until cantidad) {
            val fila = FILAS - 1 - i
            val hueco = (0 until COLUMNAS).random()
            for (columna in 0 until COLUMNAS) {
                nuevo[fila][columna] = if (columna == hueco) 0 else COLOR_BASURA
            }
        }
        return nuevo
    }

    fun caidaInstantanea(tablero: Tablero, pieza: Pieza): Pieza {
        var actual = pieza
        while (posicionValida(tablero, actual.movida(1, 0))) {
            actual = actual.movida(1, 0)
        }
        return actual
    }
}
