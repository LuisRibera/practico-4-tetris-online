package com.example.tetrisduel.repositories

import com.example.tetrisduel.socket.EventoSocket
import com.example.tetrisduel.socket.SocketManager
import kotlinx.coroutines.flow.SharedFlow

class JuegoRepository(private val socketManager: SocketManager) {

    val eventos: SharedFlow<EventoSocket> = socketManager.eventos

    fun conectar(url: String) = socketManager.conectar(url)

    fun crearSala() = socketManager.crearSala()

    fun unirseSala(codigo: String) = socketManager.unirseSala(codigo)

    fun enviarAtaque(codigo: String, lineasBasura: Int) =
        socketManager.enviarAtaque(codigo, lineasBasura)

    fun notificarDerrota(codigo: String) = socketManager.notificarDerrota(codigo)

    fun desconectar() = socketManager.desconectar()
}
