package com.nexauren.scanflow.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val ScanBlue = Color(0xFF16A9F5)
private val ScanCyan = Color(0xFF24D1F4)
private val ScanViolet = Color(0xFF8068F7)

private val DarkColors = darkColorScheme(
    primary = ScanBlue,
    onPrimary = Color.White,
    secondary = ScanCyan,
    tertiary = ScanViolet,
    background = Color(0xFF101827),
    surface = Color(0xFF172338),
    surfaceVariant = Color(0xFF202E45),
    onBackground = Color(0xFFF3F7FC),
    onSurface = Color(0xFFF3F7FC),
    onSurfaceVariant = Color(0xFFB4C2D5),
    outline = Color(0xFF34445D)
)

private val LightColors = lightColorScheme(
    primary = Color(0xFF087FD0),
    onPrimary = Color.White,
    secondary = Color(0xFF11AFCF),
    tertiary = ScanViolet,
    background = Color(0xFFF1F5FA),
    surface = Color.White,
    surfaceVariant = Color(0xFFE8EEF6),
    onBackground = Color(0xFF14253B),
    onSurface = Color(0xFF14253B),
    onSurfaceVariant = Color(0xFF60758D),
    outline = Color(0xFFD4DFEB)
)

@Composable
fun ScanFlowTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}
