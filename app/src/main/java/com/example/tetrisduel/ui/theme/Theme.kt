package com.example.tetrisduel.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val EsquemaOscuro = darkColorScheme(
    primary = AcentoCian,
    secondary = AcentoMagenta,
    background = FondoOscuro,
    surface = SuperficieOscura,
    onPrimary = FondoOscuro,
    onBackground = TextoClaro,
    onSurface = TextoClaro
)

@Composable
fun TetrisDuelTheme(
    oscuro: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = EsquemaOscuro,
        typography = Tipografia,
        content = content
    )
}
