package fr.c1.chatbot.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
class ColorSchemeExtension (
    val bot: Color = Color.Unspecified,
    val user: Color = Color.Unspecified,
    val placeHolder: Color = Color.Unspecified
)

val LightColorSchemeExtension = ColorSchemeExtension(
    bot = Color.Gray,
    user = Color.Blue,
    placeHolder = Gray600
)

val DarkColorSchemeExtension = ColorSchemeExtension(
    bot = Color.Gray,
    user = Color.Blue,
    placeHolder = Gray400
)

fun DynamicColorSchemeExtension(colorScheme: ColorScheme) = ColorSchemeExtension(
    bot = colorScheme.primary,
    user = colorScheme.secondary,
    placeHolder = Color.LightGray
)

val LocalColorSchemeExtension = staticCompositionLocalOf { ColorSchemeExtension() }

val MaterialTheme.colorSchemeExtension
    @Composable
    @ReadOnlyComposable
    get() = LocalColorSchemeExtension.current