package com.example.tetrisduel.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetrisduel.repositories.JuegoRepository
import com.example.tetrisduel.socket.EventoSocket
import com.example.tetrisduel.ui.states.EstadoSala
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SalaViewModel @Inject constructor(
    private val repositorio: JuegoRepository
) : ViewModel() {

    private enum class AccionPendiente {
        CREAR_SALA,
        UNIRSE_SALA
    }

    private val _estado = MutableStateFlow(EstadoSala())
    val estado: StateFlow<EstadoSala> = _estado.asStateFlow()
    private var accionPendiente: AccionPendiente? = null

    init {
        escucharEventos()
    }

    private fun escucharEventos() = viewModelScope.launch {
        repositorio.eventos.collect { evento ->
            when (evento) {
                is EventoSocket.Conectado -> {
                    _estado.value = _estado.value.copy(
                        conectado = true,
                        conectando = false,
                        mensaje = null
                    )
                    ejecutarAccionPendiente()
                }

                is EventoSocket.Desconectado ->
                    _estado.value = _estado.value.copy(
                        conectado = false,
                        conectando = false
                    )

                is EventoSocket.SalaCreada ->
                    _estado.value = _estado.value.copy(
                        codigoSala = evento.codigo,
                        esperandoOponente = true,
                        mensaje = "Sala creada: ${evento.codigo}"
                    )

                is EventoSocket.PartidaIniciada ->
                    _estado.value = _estado.value.copy(
                        partidaIniciada = true,
                        esperandoOponente = false,
                        codigoSala = _estado.value.codigoSala ?: _estado.value.codigoIngresado
                    )

                is EventoSocket.Error -> {
                    accionPendiente = null
                    _estado.value = _estado.value.copy(
                        conectando = false,
                        esperandoOponente = false,
                        mensaje = evento.mensaje
                    )
                }

                else -> Unit
            }
        }
    }

    fun cambiarUrl(valor: String) {
        _estado.value = _estado.value.copy(
            url = valor,
            conectado = false,
            conectando = false,
            mensaje = null
        )
    }

    fun cambiarCodigo(valor: String) {
        _estado.value = _estado.value.copy(
            codigoIngresado = valor.uppercase(),
            mensaje = null
        )
    }

    fun conectar() {
        val url = _estado.value.url.trim()
        if (url.isBlank()) {
            _estado.value = _estado.value.copy(mensaje = "Ingresa una URL valida")
            accionPendiente = null
            return
        }
        if (repositorio.estaConectado(url)) {
            _estado.value = _estado.value.copy(
                conectado = true,
                conectando = false,
                mensaje = null
            )
            ejecutarAccionPendiente()
            return
        }
        _estado.value = _estado.value.copy(
            conectado = false,
            conectando = true,
            mensaje = null
        )
        repositorio.conectar(url)
    }

    fun crearSala() {
        accionPendiente = AccionPendiente.CREAR_SALA
        conectar()
    }

    fun unirseSala() {
        val codigo = _estado.value.codigoIngresado.trim()
        if (codigo.isBlank()) {
            _estado.value = _estado.value.copy(mensaje = "Ingresa un codigo de sala")
            accionPendiente = null
            return
        }
        accionPendiente = AccionPendiente.UNIRSE_SALA
        conectar()
    }

    private fun ejecutarAccionPendiente() {
        when (accionPendiente) {
            AccionPendiente.CREAR_SALA -> {
                accionPendiente = null
                repositorio.crearSala()
            }

            AccionPendiente.UNIRSE_SALA -> {
                val codigo = _estado.value.codigoIngresado.trim()
                if (codigo.isBlank()) {
                    accionPendiente = null
                    _estado.value = _estado.value.copy(mensaje = "Ingresa un codigo de sala")
                } else {
                    accionPendiente = null
                    repositorio.unirseSala(codigo)
                }
            }

            null -> Unit
        }
    }
}
