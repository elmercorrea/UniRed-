package com.example.unired.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Esquema de Colores para MODO CLARO
private val LightColorScheme = lightColorScheme(
    primary = UniRedPrimary,
    onPrimary = Color.White,
    secondary = UniRedPrimaryVariant,
    onSecondary = Color.White,
    tertiary = UniRedAccent,
    background = UniRedBackground,
    onBackground = UniRedTextPrimary,
    surface = UniRedSurface,
    onSurface = UniRedTextPrimary,
    error = UniRedError,
    onError = Color.White
)

// Esquema de Colores para MODO OSCURO
private val DarkColorScheme = darkColorScheme(
    primary = UniRedPrimaryVariant,
    onPrimary = Color.White,
    secondary = UniRedAccent,
    onSecondary = Color.White,
    tertiary = UniRedPrimary,
    background = Color(0xFF0F172A),          // Fondo azul muy oscuro
    onBackground = Color(0xFFF8FAFC),        // Texto claro
    surface = Color(0xFF1E293B),             // Tarjetas oscuras
    onSurface = Color(0xFFF8FAFC),
    error = UniRedError,
    onError = Color.White
)

@Composable
fun UniRedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // Detecta automáticamente el tema del sistema
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}