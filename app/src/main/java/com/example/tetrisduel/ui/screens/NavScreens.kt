package com.example.tetrisduel.ui.screens

enum class NavScreens(val ruta: String) {
    Sala("sala"),
    Juego("juego/{codigoSala}");

    companion object {
        const val argumentoCodigoSala = "codigoSala"

        fun rutaJuego(codigoSala: String): String = "juego/$codigoSala"
    }
}
