package com.pdfking.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF7C4DFF),
    secondary = androidx.compose.ui.graphics.Color(0xFF9575CD),
    background = androidx.compose.ui.graphics.Color(0xFFF3E5F5)
)

private val DarkColors = darkColorScheme(
    primary = androidx.compose.ui.graphics.Color(0xFF7C4DFF),
    secondary = androidx.compose.ui.graphics.Color(0xFF9575CD)
)

@Composable
fun PdfKingTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = LightColors,
        typography = androidx.compose.material3.Typography(),
        content = content
    )
}
