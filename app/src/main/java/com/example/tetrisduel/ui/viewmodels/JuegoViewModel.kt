package com.example.tetrisduel.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tetrisduel.game.MotorTetris
import com.example.tetrisduel.game.Pieza
import com.example.tetrisduel.game.TablaAtaque
import com.example.tetrisduel.game.TipoPieza
import com.example.tetrisduel.repositories.JuegoRepository
import com.example.tetrisduel.socket.EventoSocket
import com.example.tetrisduel.ui.states.EstadoJuego
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JuegoViewModel @Inject constructor(
    private val repositorio: JuegoRepository
) : ViewModel() {

    private val _estado = MutableStateFlow(EstadoJuego())
    val estado: StateFlow<EstadoJuego> = _estado.asStateFlow()

    private var codigo: String = ""
    private var inicioMs: Long = 0
    private var bucle: Job? = null
    private var escuchaEventos: Job? = null
    private val velocidadCaida = 600L

    fun iniciar(codigoSala: String) {
        if (codigo.isNotEmpty()) return
        codigo = codigoSala
        inicioMs = System.currentTimeMillis()
        val primera = MotorTetris.posicionInicial(TipoPieza.aleatoria())
        _estado.value = EstadoJuego(
            piezaActual = primera,
            siguiente = TipoPieza.aleatoria()
        )
        escuchaEventos?.cancel()
        escuchaEventos = escucharEventos()
        iniciarBucle()
    }

    private fun escucharEventos() = viewModelScope.launch {
        repositorio.eventos.collect { evento ->
            when (evento) {
                is EventoSocket.AtaqueRecibido -> recibirAtaque(evento.lineasBasura)
                is EventoSocket.Victoria -> terminarPartida(
                    gano = true,
                    mensaje = "Ganaste la partida"
                )

                is EventoSocket.OponenteDesconectado -> {
                    _estado.value = _estado.value.copy(oponenteConectado = false)
                    terminarPartida(
                        gano = true,
                        mensaje = "Tu oponente se desconecto"
                    )
                }

                is EventoSocket.Desconectado ->
                    _estado.value = _estado.value.copy(conectado = false)

                is EventoSocket.Conectado ->
                    _estado.value = _estado.value.copy(conectado = true)

                else -> Unit
            }
        }
    }

    private fun iniciarBucle() {
        bucle?.cancel()
        bucle = viewModelScope.launch {
            while (!_estado.value.terminado) {
                delay(velocidadCaida)
                if (!_estado.value.terminado) descender()
            }
        }
    }

    private fun descender() {
        val pieza = _estado.value.piezaActual ?: return
        val abajo = pieza.movida(1, 0)
        if (MotorTetris.posicionValida(_estado.value.tablero, abajo)) {
            _estado.value = _estado.value.copy(piezaActual = abajo)
        } else {
            bloquear(pieza)
        }
    }

    private fun bloquear(pieza: Pieza) {
        val tableroFijado = MotorTetris.fijar(_estado.value.tablero, pieza)
        val (tableroLimpio, eliminadas) = MotorTetris.limpiarLineas(tableroFijado)

        val puntajeAnterior = _estado.value.puntaje
        val puntajeNuevo = puntajeAnterior + TablaAtaque.puntaje(eliminadas)
        val lucky = puntajeNuevo / 37 > puntajeAnterior / 37 && puntajeNuevo > 0
        val basura = TablaAtaque.lineasBasura(eliminadas) + if (lucky) 1 else 0

        if (basura > 0) repositorio.enviarAtaque(codigo, basura)

        val nuevaPieza = MotorTetris.posicionInicial(_estado.value.siguiente)
        if (!MotorTetris.posicionValida(tableroLimpio, nuevaPieza)) {
            _estado.value = _estado.value.copy(
                tablero = tableroLimpio,
                piezaActual = null,
                puntaje = puntajeNuevo,
                lineas = _estado.value.lineas + eliminadas,
                piezasColocadas = _estado.value.piezasColocadas + 1
            )
            terminarPartida(
                gano = false,
                mensaje = "Perdiste la partida"
            )
            return
        }

        _estado.value = _estado.value.copy(
            tablero = tableroLimpio,
            piezaActual = nuevaPieza,
            siguiente = TipoPieza.aleatoria(),
            puntaje = puntajeNuevo,
            lineas = _estado.value.lineas + eliminadas,
            piezasColocadas = _estado.value.piezasColocadas + 1,
            mostrarLucky37 = lucky
        )
        if (lucky) ocultarLucky37()
    }

    private fun ocultarLucky37() = viewModelScope.launch {
        delay(1200)
        _estado.value = _estado.value.copy(mostrarLucky37 = false)
    }

    private fun recibirAtaque(cantidad: Int) {
        val tableroConBasura = MotorTetris.agregarBasura(_estado.value.tablero, cantidad)
        val piezaActual = _estado.value.piezaActual
        var piezaAjustada = piezaActual
        if (piezaActual != null) {
            var pieza: Pieza = piezaActual
            while (!MotorTetris.posicionValida(tableroConBasura, pieza) && pieza.fila > 0) {
                pieza = pieza.movida(-1, 0)
            }
            piezaAjustada = pieza
        }
        _estado.value = _estado.value.copy(
            tablero = tableroConBasura,
            piezaActual = piezaAjustada
        )
    }

    fun moverIzquierda() = intentarMovimiento(0, -1)

    fun moverDerecha() = intentarMovimiento(0, 1)

    fun bajar() = intentarMovimiento(1, 0)

    private fun intentarMovimiento(filas: Int, columnas: Int) {
        val pieza = _estado.value.piezaActual ?: return
        if (_estado.value.terminado) return
        val movida = pieza.movida(filas, columnas)
        if (MotorTetris.posicionValida(_estado.value.tablero, movida)) {
            _estado.value = _estado.value.copy(piezaActual = movida)
        }
    }

    fun rotar() {
        val pieza = _estado.value.piezaActual ?: return
        if (_estado.value.terminado) return
        val rotada = pieza.rotada()
        if (MotorTetris.posicionValida(_estado.value.tablero, rotada)) {
            _estado.value = _estado.value.copy(piezaActual = rotada)
        }
    }

    fun caidaInstantanea() {
        val pieza = _estado.value.piezaActual ?: return
        if (_estado.value.terminado) return
        val abajo = MotorTetris.caidaInstantanea(_estado.value.tablero, pieza)
        bloquear(abajo)
    }

    private fun terminarPartida(gano: Boolean, mensaje: String) {
        if (_estado.value.terminado) return
        bucle?.cancel()
        if (!gano) repositorio.notificarDerrota(codigo)
        val duracion = ((System.currentTimeMillis() - inicioMs) / 1000).toInt()
        _estado.value = _estado.value.copy(
            terminado = true,
            gano = gano,
            duracionSegundos = duracion,
            mensajeFinal = mensaje
        )
    }

    override fun onCleared() {
        super.onCleared()
        bucle?.cancel()
        escuchaEventos?.cancel()
    }
}
