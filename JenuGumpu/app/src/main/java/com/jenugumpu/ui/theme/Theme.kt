package com.jenugumpu.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF5D4037),
    onPrimary = Color.White,

    secondary = Color(0xFF6A994E),
    onSecondary = Color.White,

    tertiary = Color(0xFFF2C94C),
    onTertiary = Color.Black,

    background = Color(0xFFFFFBF5),
    surface = Color.White,
    surfaceVariant = Color(0xFFF1EAE4),

    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),

    outline = Color(0xFFBCAAA4)
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFF4C95D),
    onPrimary = Color.Black,
    secondary = Color(0xFF6A994E),
    onSecondary = Color.White,
    tertiary = Color(0xFF6D4C41),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    surfaceVariant = Color(0xFF2C2C2C),
    onBackground = Color(0xFFECECEC),
    onSurface = Color(0xFFECECEC),
    outline = Color(0xFF6D6D6D)
)

@Composable
fun JenuGumpuTheme(darkTheme: Boolean = false, content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        typography = Typography,
        content = content
    )
}
