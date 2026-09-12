package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TrendyDarkColorScheme = darkColorScheme(
    primary = TrendyNeonMagenta,
    onPrimary = Color.White,
    primaryContainer = TrendyNeonPurple,
    onPrimaryContainer = Color.White,
    secondary = TrendyNeonCyan,
    onSecondary = Color.Black,
    secondaryContainer = Color(0xFF1E3A40),
    onSecondaryContainer = TrendyNeonCyan,
    tertiary = TrendyNeonPurple,
    background = TrendyDeepBlack,
    onBackground = TrendyTextPrimary,
    surface = TrendySurfaceDark,
    onSurface = TrendyTextPrimary,
    surfaceVariant = TrendySurfaceCard,
    onSurfaceVariant = TrendyTextSecondary,
    error = TrendyLikeRed,
    onError = Color.White
)

private val TrendyLightColorScheme = lightColorScheme(
    primary = TrendyNeonMagenta,
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFFD9E6),
    onPrimaryContainer = Color(0xFF3E001F),
    secondary = Color(0xFF007A8A),
    onSecondary = Color.White,
    background = TrendyLightBackground,
    onBackground = TrendyLightTextPrimary,
    surface = TrendyLightSurface,
    onSurface = TrendyLightTextPrimary,
    surfaceVariant = TrendyLightSurfaceVariant,
    onSurfaceVariant = TrendyLightTextSecondary,
    error = TrendyLikeRed,
    onError = Color.White
)

@Composable
fun TrendyTheme(
    themeMode: String = "dark",
    content: @Composable () -> Unit
) {
    val isDark = when (themeMode.lowercase()) {
        "light" -> false
        "dark" -> true
        else -> isSystemInDarkTheme()
    }

    val colorScheme = if (isDark) TrendyDarkColorScheme else TrendyLightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
