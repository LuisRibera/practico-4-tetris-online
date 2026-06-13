package com.example.tetrisduel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tetrisduel.ui.screens.JuegoScreen
import com.example.tetrisduel.ui.screens.NavScreens
import com.example.tetrisduel.ui.screens.ResultadoScreen
import com.example.tetrisduel.ui.screens.SalaScreen
import com.example.tetrisduel.ui.theme.TetrisDuelTheme
import com.example.tetrisduel.ui.viewmodels.JuegoViewModel
import com.example.tetrisduel.ui.viewmodels.ResultadoViewModel
import com.example.tetrisduel.ui.viewmodels.SalaViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TetrisDuelTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = NavScreens.Sala.ruta
                    ) {
                        composable(NavScreens.Sala.ruta) {
                            val viewModel: SalaViewModel = hiltViewModel()
                            val estado by viewModel.estado.collectAsState()
                            SalaScreen(
                                estado = estado,
                                onCambiarUrl = viewModel::cambiarUrl,
                                onCambiarCodigo = viewModel::cambiarCodigo,
                                onConectar = viewModel::conectar,
                                onCrearSala = viewModel::crearSala,
                                onUnirseSala = viewModel::unirseSala,
                                onIrAJuego = { codigoSala ->
                                    navController.navigate(NavScreens.rutaJuego(codigoSala)) {
                                        popUpTo(NavScreens.Sala.ruta) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        composable(
                            route = NavScreens.Juego.ruta,
                            arguments = listOf(
                                navArgument(NavScreens.argumentoCodigoSala) {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val viewModel: JuegoViewModel = hiltViewModel()
                            val estado by viewModel.estado.collectAsState()
                            JuegoScreen(
                                codigoSala = backStackEntry.arguments
                                    ?.getString(NavScreens.argumentoCodigoSala)
                                    .orEmpty(),
                                estado = estado,
                                onIniciar = viewModel::iniciar,
                                onMoverIzquierda = viewModel::moverIzquierda,
                                onRotar = viewModel::rotar,
                                onMoverDerecha = viewModel::moverDerecha,
                                onBajar = viewModel::bajar,
                                onCaidaInstantanea = viewModel::caidaInstantanea,
                                onIrAResultado = { gano, puntaje, lineas, duracion, mensaje ->
                                    navController.navigate(
                                        NavScreens.rutaResultado(
                                            gano = gano,
                                            puntaje = puntaje,
                                            lineas = lineas,
                                            duracion = duracion,
                                            mensaje = mensaje
                                        )
                                    ) {
                                        popUpTo(NavScreens.Juego.ruta) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                        composable(
                            route = NavScreens.Resultado.ruta,
                            arguments = listOf(
                                navArgument(NavScreens.argumentoGano) {
                                    type = NavType.BoolType
                                },
                                navArgument(NavScreens.argumentoPuntaje) {
                                    type = NavType.IntType
                                },
                                navArgument(NavScreens.argumentoLineas) {
                                    type = NavType.IntType
                                },
                                navArgument(NavScreens.argumentoDuracion) {
                                    type = NavType.IntType
                                },
                                navArgument(NavScreens.argumentoMensaje) {
                                    type = NavType.StringType
                                }
                            )
                        ) { backStackEntry ->
                            val viewModel: ResultadoViewModel = hiltViewModel()
                            ResultadoScreen(
                                gano = backStackEntry.arguments
                                    ?.getBoolean(NavScreens.argumentoGano)
                                    ?: false,
                                puntaje = backStackEntry.arguments
                                    ?.getInt(NavScreens.argumentoPuntaje)
                                    ?: 0,
                                lineas = backStackEntry.arguments
                                    ?.getInt(NavScreens.argumentoLineas)
                                    ?: 0,
                                duracion = backStackEntry.arguments
                                    ?.getInt(NavScreens.argumentoDuracion)
                                    ?: 0,
                                mensaje = backStackEntry.arguments
                                    ?.getString(NavScreens.argumentoMensaje)
                                    .orEmpty(),
                                onVolverASala = {
                                    viewModel.desconectar()
                                    navController.navigate(NavScreens.Sala.ruta) {
                                        popUpTo(NavScreens.Resultado.ruta) {
                                            inclusive = true
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
