package net.osdn.ja.gokigen.transporthub.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun GokigenComposeAppsTheme(
    //darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable() () -> Unit
) {
    MaterialTheme(
        colors = defaultColorPalette,
        typography = Typography,
        content = content
    )
}
