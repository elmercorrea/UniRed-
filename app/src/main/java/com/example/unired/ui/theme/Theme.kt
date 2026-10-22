package com.example.unired.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


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


private val DarkColorScheme = darkColorScheme(
    primary = UniRedPrimaryVariant,
    onPrimary = Color.White,
    secondary = UniRedAccent,
    onSecondary = Color.White,
    tertiary = UniRedPrimary,
    background = Color(0xFF0F172A),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),
    onSurface = Color(0xFFF8FAFC),
    error = UniRedError,
    onError = Color.White
)

@Composable
fun UniRedTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}