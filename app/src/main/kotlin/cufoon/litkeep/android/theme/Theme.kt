package cufoon.litkeep.android.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import com.google.accompanist.systemuicontroller.rememberSystemUiController

private val lightColors = lightColorScheme(
    primary = LightColorPrimary,
    onPrimary = LightColorPrimaryOn,
    primaryContainer = LightColorPrimaryContainer,
    onPrimaryContainer = LightColorPrimaryContainerOn,
    inversePrimary = LightColorPrimaryInverse,
    secondary = LightColorSecondary,
    onSecondary = LightColorSecondaryOn,
    secondaryContainer = LightColorSecondaryContainer,
    onSecondaryContainer = LightColorSecondaryContainerOn,
    tertiary = LightColorTertiary,
    onTertiary = LightColorTertiaryOn,
    tertiaryContainer = LightColorTertiaryContainer,
    onTertiaryContainer = LightColorTertiaryContainerOn,
    error = LightColorError,
    onError = LightColorErrorOn,
    errorContainer = LightColorErrorContainer,
    onErrorContainer = LightColorErrorContainerOn,
    background = LightColorBackground,
    onBackground = LightColorBackgroundOn,
    surface = LightColorSurface,
    onSurface = LightColorSurfaceOn,
    surfaceVariant = LightColorSurfaceVariant,
    onSurfaceVariant = LightColorSurfaceVariantOn,
    inverseSurface = LightColorSurfaceInverse,
    inverseOnSurface = LightColorSurfaceInverseOn,
    surfaceTint = LightColorSurfaceTint,
    outline = LightColorOutline,
    outlineVariant = LightColorOutlineVariant,
    scrim = LightColorScrim,
)


private val darkColors = darkColorScheme(
    primary = DarkColorPrimary,
    onPrimary = DarkColorPrimaryOn,
    primaryContainer = DarkColorPrimaryContainer,
    onPrimaryContainer = DarkColorPrimaryContainerOn,
    inversePrimary = DarkColorPrimaryInverse,
    secondary = DarkColorSecondary,
    onSecondary = DarkColorSecondaryOn,
    secondaryContainer = DarkColorSecondaryContainer,
    onSecondaryContainer = DarkColorSecondaryContainerOn,
    tertiary = DarkColorTertiary,
    onTertiary = DarkColorTertiaryOn,
    tertiaryContainer = DarkColorTertiaryContainer,
    onTertiaryContainer = DarkColorTertiaryContainerOn,
    error = DarkColorError,
    onError = DarkColorErrorOn,
    errorContainer = DarkColorErrorContainer,
    onErrorContainer = DarkColorErrorContainerOn,
    background = DarkColorBackground,
    onBackground = DarkColorBackgroundOn,
    surface = DarkColorSurface,
    onSurface = DarkColorSurfaceOn,
    surfaceVariant = DarkColorSurfaceVariant,
    onSurfaceVariant = DarkColorSurfaceVariantOn,
    inverseSurface = DarkColorSurfaceInverse,
    inverseOnSurface = DarkColorSurfaceInverseOn,
    surfaceTint = DarkColorSurfaceTint,
    outline = DarkColorOutline,
    outlineVariant = DarkColorOutlineVariant,
    scrim = DarkColorScrim,
)

//val dynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S


@Composable
fun LitKeepTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(LitColors.WhiteBackground)

    val colorScheme = when {
//        dynamicColor && darkTheme -> dynamicDarkColorScheme(LocalContext.current)
//        dynamicColor && !darkTheme -> dynamicLightColorScheme(LocalContext.current)
        darkTheme -> darkColors
        else -> lightColors
    }

    MaterialTheme(
        colorScheme = colorScheme, typography = Typography, shapes = Shapes, content = content
    )
}