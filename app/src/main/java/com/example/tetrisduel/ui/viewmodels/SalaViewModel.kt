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

    private val _estado = MutableStateFlow(EstadoSala())
    val estado: StateFlow<EstadoSala> = _estado.asStateFlow()

    init {
        escucharEventos()
    }

    private fun escucharEventos() = viewModelScope.launch {
        repositorio.eventos.collect { evento ->
            when (evento) {
                is EventoSocket.Conectado ->
                    _estado.value = _estado.value.copy(conectado = true, mensaje = null)

                is EventoSocket.Desconectado ->
                    _estado.value = _estado.value.copy(conectado = false)

                is EventoSocket.SalaCreada ->
                    _estado.value = _estado.value.copy(
                        codigoSala = evento.codigo,
                        esperandoOponente = true
                    )

                is EventoSocket.PartidaIniciada ->
                    _estado.value = _estado.value.copy(
                        partidaIniciada = true,
                        codigoSala = _estado.value.codigoSala ?: _estado.value.codigoIngresado
                    )

                is EventoSocket.Error ->
                    _estado.value = _estado.value.copy(mensaje = evento.mensaje)

                else -> Unit
            }
        }
    }

    fun cambiarUrl(valor: String) {
        _estado.value = _estado.value.copy(url = valor)
    }

    fun cambiarCodigo(valor: String) {
        _estado.value = _estado.value.copy(codigoIngresado = valor.uppercase())
    }

    fun conectar() {
        repositorio.conectar(_estado.value.url.trim())
    }

    fun crearSala() {
        conectar()
        repositorio.crearSala()
    }

    fun unirseSala() {
        conectar()
        repositorio.unirseSala(_estado.value.codigoIngresado.trim())
    }
}
