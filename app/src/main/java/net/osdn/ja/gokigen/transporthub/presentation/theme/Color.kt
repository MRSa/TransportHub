package net.osdn.ja.gokigen.transporthub.presentation.theme

import androidx.compose.ui.graphics.Color
import androidx.wear.compose.material.Colors

val Blue200 = Color(0xFFAECBFA)
val Blue300 = Color(0xFF8AB4F8)
val Yellow200 = Color(0xFFFdE293)
val Yellow300 = Color(0xFFFDD663)
val Black = Color(0xFF000000)
val DarkGrey = Color(0XFF303133)
val Red400 = Color(0xFFEE675C)
val White = Color(0xFFFFFFFF)
val Grey300 = Color(0xFFDADCE0)


internal val wearColorPalette: Colors = Colors(
    primary = Blue200,
    primaryVariant = Blue300,
    secondary = Yellow200,
    secondaryVariant = Yellow300,
    error = Red400,
    surface = DarkGrey,
    onPrimary = DarkGrey,
    onSecondary = DarkGrey,
    onError = Black,
    onSurfaceVariant = Grey300,
    onBackground = Black,
    onSurface = White,
)
