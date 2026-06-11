package com.example.tetrisduel.ui.states

data class EstadoSala(
    val url: String = "http://10.0.2.2:3000",
    val codigoIngresado: String = "",
    val codigoSala: String? = null,
    val conectado: Boolean = false,
    val esperandoOponente: Boolean = false,
    val partidaIniciada: Boolean = false,
    val mensaje: String? = null
)
