package com.example.tetrisduel.socket

sealed class EventoSocket {
    data object Conectado : EventoSocket()
    data object Desconectado : EventoSocket()
    data class SalaCreada(val codigo: String) : EventoSocket()
    data object PartidaIniciada : EventoSocket()
    data class AtaqueRecibido(val lineasBasura: Int) : EventoSocket()
    data object Victoria : EventoSocket()
    data object OponenteDesconectado : EventoSocket()
    data class Error(val mensaje: String) : EventoSocket()
}
