package com.example.tetrisduel.socket

import io.socket.client.IO
import io.socket.client.Socket
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import org.json.JSONObject
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SocketManager @Inject constructor() {

    private var socket: Socket? = null

    private val _eventos = MutableSharedFlow<EventoSocket>(extraBufferCapacity = 32)
    val eventos: SharedFlow<EventoSocket> = _eventos.asSharedFlow()

    fun conectar(url: String) {
        if (socket?.connected() == true) return
        val nuevoSocket = IO.socket(url)
        registrarEventos(nuevoSocket)
        socket = nuevoSocket
        nuevoSocket.connect()
    }

    private fun registrarEventos(socket: Socket) {
        socket.on(Socket.EVENT_CONNECT) {
            _eventos.tryEmit(EventoSocket.Conectado)
        }
        socket.on(Socket.EVENT_DISCONNECT) {
            _eventos.tryEmit(EventoSocket.Desconectado)
        }
        socket.on("room_created") { args ->
            val datos = args[0] as JSONObject
            _eventos.tryEmit(EventoSocket.SalaCreada(datos.getString("roomId")))
        }
        socket.on("game_start") {
            _eventos.tryEmit(EventoSocket.PartidaIniciada)
        }
        socket.on("receive_attack") { args ->
            val datos = args[0] as JSONObject
            _eventos.tryEmit(EventoSocket.AtaqueRecibido(datos.getInt("garbageLines")))
        }
        socket.on("victory") {
            _eventos.tryEmit(EventoSocket.Victoria)
        }
        socket.on("opponent_disconnected") {
            _eventos.tryEmit(EventoSocket.OponenteDesconectado)
        }
        socket.on("error_message") { args ->
            val datos = args[0] as JSONObject
            _eventos.tryEmit(EventoSocket.Error(datos.getString("message")))
        }
    }

    fun crearSala() {
        socket?.emit("create_room")
    }

    fun unirseSala(codigo: String) {
        socket?.emit("join_room", JSONObject().put("roomId", codigo))
    }

    fun enviarAtaque(codigo: String, lineasBasura: Int) {
        val datos = JSONObject()
            .put("roomId", codigo)
            .put("garbageLines", lineasBasura)
        socket?.emit("send_attack", datos)
    }

    fun notificarDerrota(codigo: String) {
        socket?.emit("game_over", JSONObject().put("roomId", codigo))
    }

    fun desconectar() {
        socket?.disconnect()
        socket?.off()
        socket = null
    }
}
