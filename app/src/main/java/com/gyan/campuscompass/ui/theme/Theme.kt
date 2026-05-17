package com.gyan.campuscompass.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    onPrimary = Color.White,
    primaryContainer = LightBlueSurface,
    onPrimaryContainer = PrimaryBlue,
    secondary = MutedBlueGrey,
    onSecondary = Color.White,
    background = WhiteBackground,
    onBackground = Color.Black,
    surface = WhiteBackground,
    onSurface = Color.Black,
    surfaceVariant = LightBlueSurface,
    onSurfaceVariant = MutedBlueGrey,
    error = EmergencyRed,
    onError = Color.White
)

@Composable
fun CampusCompassTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
