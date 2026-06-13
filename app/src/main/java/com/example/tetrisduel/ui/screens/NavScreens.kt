package com.example.tetrisduel.ui.screens

import android.net.Uri

enum class NavScreens(val ruta: String) {
    Sala("sala"),
    Juego("juego/{codigoSala}"),
    Resultado("resultado/{gano}/{puntaje}/{lineas}/{duracion}/{mensaje}");

    companion object {
        const val argumentoCodigoSala = "codigoSala"
        const val argumentoGano = "gano"
        const val argumentoPuntaje = "puntaje"
        const val argumentoLineas = "lineas"
        const val argumentoDuracion = "duracion"
        const val argumentoMensaje = "mensaje"

        fun rutaJuego(codigoSala: String): String = "juego/$codigoSala"

        fun rutaResultado(
            gano: Boolean,
            puntaje: Int,
            lineas: Int,
            duracion: Int,
            mensaje: String
        ): String = "resultado/$gano/$puntaje/$lineas/$duracion/${Uri.encode(mensaje)}"
    }
}
