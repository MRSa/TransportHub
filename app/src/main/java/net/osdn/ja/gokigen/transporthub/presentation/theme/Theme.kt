package net.osdn.ja.gokigen.transporthub.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun GokigenComposeAppsTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    val colors = wearColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        content = content
    )
}
