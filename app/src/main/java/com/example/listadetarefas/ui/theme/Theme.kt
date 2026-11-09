package com.example.listadetarefas.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val Black = Color(0xFF000000)
val NavyDark = Color(0xFF0A1226)
val Navy = Color(0xFF101D3B)
val NavyLight = Color(0xFF2D4F9E)
val White = Color(0xFFFFFFFF)
val Muted = Color(0xFF8E9AB5)

private val Scheme = darkColorScheme(
    primary = NavyLight,
    onPrimary = White,
    background = Black,
    onBackground = White,
    surface = Navy,
    onSurface = White,
    surfaceVariant = Navy,
    onSurfaceVariant = Muted,
    secondaryContainer = Navy,
    onSecondaryContainer = White
)

@Composable
fun TaskTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = Scheme, content = content)
}
