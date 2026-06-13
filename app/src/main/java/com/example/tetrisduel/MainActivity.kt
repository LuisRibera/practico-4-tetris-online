package com.example.tetrisduel

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxSize
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tetrisduel.ui.screens.JuegoScreen
import com.example.tetrisduel.ui.screens.NavScreens
import com.example.tetrisduel.ui.screens.SalaScreen
import com.example.tetrisduel.ui.theme.TetrisDuelTheme
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
                            SalaScreen(
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
                            JuegoScreen(
                                codigoSala = backStackEntry.arguments
                                    ?.getString(NavScreens.argumentoCodigoSala)
                                    .orEmpty()
                            )
                        }
                    }
                }
            }
        }
    }
}
