

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.athkar.sa.compose.Typography
import com.athkar.sa.compose.backgroundDark
import com.athkar.sa.compose.backgroundDarkHighContrast
import com.athkar.sa.compose.backgroundDarkMediumContrast
import com.athkar.sa.compose.backgroundLight
import com.athkar.sa.compose.backgroundLightHighContrast
import com.athkar.sa.compose.backgroundLightMediumContrast
import com.athkar.sa.compose.errorContainerDark
import com.athkar.sa.compose.errorContainerDarkHighContrast
import com.athkar.sa.compose.errorContainerDarkMediumContrast
import com.athkar.sa.compose.errorContainerLight
import com.athkar.sa.compose.errorContainerLightHighContrast
import com.athkar.sa.compose.errorContainerLightMediumContrast
import com.athkar.sa.compose.errorDark
import com.athkar.sa.compose.errorDarkHighContrast
import com.athkar.sa.compose.errorDarkMediumContrast
import com.athkar.sa.compose.errorLight
import com.athkar.sa.compose.errorLightHighContrast
import com.athkar.sa.compose.errorLightMediumContrast
import com.athkar.sa.compose.inverseOnSurfaceDark
import com.athkar.sa.compose.inverseOnSurfaceDarkHighContrast
import com.athkar.sa.compose.inverseOnSurfaceDarkMediumContrast
import com.athkar.sa.compose.inverseOnSurfaceLight
import com.athkar.sa.compose.inverseOnSurfaceLightHighContrast
import com.athkar.sa.compose.inverseOnSurfaceLightMediumContrast
import com.athkar.sa.compose.inversePrimaryDark
import com.athkar.sa.compose.inversePrimaryDarkHighContrast
import com.athkar.sa.compose.inversePrimaryDarkMediumContrast
import com.athkar.sa.compose.inversePrimaryLight
import com.athkar.sa.compose.inversePrimaryLightHighContrast
import com.athkar.sa.compose.inversePrimaryLightMediumContrast
import com.athkar.sa.compose.inverseSurfaceDark
import com.athkar.sa.compose.inverseSurfaceDarkHighContrast
import com.athkar.sa.compose.inverseSurfaceDarkMediumContrast
import com.athkar.sa.compose.inverseSurfaceLight
import com.athkar.sa.compose.inverseSurfaceLightHighContrast
import com.athkar.sa.compose.inverseSurfaceLightMediumContrast
import com.athkar.sa.compose.onBackgroundDark
import com.athkar.sa.compose.onBackgroundDarkHighContrast
import com.athkar.sa.compose.onBackgroundDarkMediumContrast
import com.athkar.sa.compose.onBackgroundLight
import com.athkar.sa.compose.onBackgroundLightHighContrast
import com.athkar.sa.compose.onBackgroundLightMediumContrast
import com.athkar.sa.compose.onErrorContainerDark
import com.athkar.sa.compose.onErrorContainerDarkHighContrast
import com.athkar.sa.compose.onErrorContainerDarkMediumContrast
import com.athkar.sa.compose.onErrorContainerLight
import com.athkar.sa.compose.onErrorContainerLightHighContrast
import com.athkar.sa.compose.onErrorContainerLightMediumContrast
import com.athkar.sa.compose.onErrorDark
import com.athkar.sa.compose.onErrorDarkHighContrast
import com.athkar.sa.compose.onErrorDarkMediumContrast
import com.athkar.sa.compose.onErrorLight
import com.athkar.sa.compose.onErrorLightHighContrast
import com.athkar.sa.compose.onErrorLightMediumContrast
import com.athkar.sa.compose.onPrimaryContainerDark
import com.athkar.sa.compose.onPrimaryContainerDarkHighContrast
import com.athkar.sa.compose.onPrimaryContainerDarkMediumContrast
import com.athkar.sa.compose.onPrimaryContainerLight
import com.athkar.sa.compose.onPrimaryContainerLightHighContrast
import com.athkar.sa.compose.onPrimaryContainerLightMediumContrast
import com.athkar.sa.compose.onPrimaryDark
import com.athkar.sa.compose.onPrimaryDarkHighContrast
import com.athkar.sa.compose.onPrimaryDarkMediumContrast
import com.athkar.sa.compose.onPrimaryLight
import com.athkar.sa.compose.onPrimaryLightHighContrast
import com.athkar.sa.compose.onPrimaryLightMediumContrast
import com.athkar.sa.compose.onSecondaryContainerDark
import com.athkar.sa.compose.onSecondaryContainerDarkHighContrast
import com.athkar.sa.compose.onSecondaryContainerDarkMediumContrast
import com.athkar.sa.compose.onSecondaryContainerLight
import com.athkar.sa.compose.onSecondaryContainerLightHighContrast
import com.athkar.sa.compose.onSecondaryContainerLightMediumContrast
import com.athkar.sa.compose.onSecondaryDark
import com.athkar.sa.compose.onSecondaryDarkHighContrast
import com.athkar.sa.compose.onSecondaryDarkMediumContrast
import com.athkar.sa.compose.onSecondaryLight
import com.athkar.sa.compose.onSecondaryLightHighContrast
import com.athkar.sa.compose.onSecondaryLightMediumContrast
import com.athkar.sa.compose.onSurfaceDark
import com.athkar.sa.compose.onSurfaceDarkHighContrast
import com.athkar.sa.compose.onSurfaceDarkMediumContrast
import com.athkar.sa.compose.onSurfaceLight
import com.athkar.sa.compose.onSurfaceLightHighContrast
import com.athkar.sa.compose.onSurfaceLightMediumContrast
import com.athkar.sa.compose.onSurfaceVariantDark
import com.athkar.sa.compose.onSurfaceVariantDarkHighContrast
import com.athkar.sa.compose.onSurfaceVariantDarkMediumContrast
import com.athkar.sa.compose.onSurfaceVariantLight
import com.athkar.sa.compose.onSurfaceVariantLightHighContrast
import com.athkar.sa.compose.onSurfaceVariantLightMediumContrast
import com.athkar.sa.compose.onTertiaryContainerDark
import com.athkar.sa.compose.onTertiaryContainerDarkHighContrast
import com.athkar.sa.compose.onTertiaryContainerDarkMediumContrast
import com.athkar.sa.compose.onTertiaryContainerLight
import com.athkar.sa.compose.onTertiaryContainerLightHighContrast
import com.athkar.sa.compose.onTertiaryContainerLightMediumContrast
import com.athkar.sa.compose.onTertiaryDark
import com.athkar.sa.compose.onTertiaryDarkHighContrast
import com.athkar.sa.compose.onTertiaryDarkMediumContrast
import com.athkar.sa.compose.onTertiaryLight
import com.athkar.sa.compose.onTertiaryLightHighContrast
import com.athkar.sa.compose.onTertiaryLightMediumContrast
import com.athkar.sa.compose.outlineDark
import com.athkar.sa.compose.outlineDarkHighContrast
import com.athkar.sa.compose.outlineDarkMediumContrast
import com.athkar.sa.compose.outlineLight
import com.athkar.sa.compose.outlineLightHighContrast
import com.athkar.sa.compose.outlineLightMediumContrast
import com.athkar.sa.compose.outlineVariantDark
import com.athkar.sa.compose.outlineVariantDarkHighContrast
import com.athkar.sa.compose.outlineVariantDarkMediumContrast
import com.athkar.sa.compose.outlineVariantLight
import com.athkar.sa.compose.outlineVariantLightHighContrast
import com.athkar.sa.compose.outlineVariantLightMediumContrast
import com.athkar.sa.compose.primaryContainerDark
import com.athkar.sa.compose.primaryContainerDarkHighContrast
import com.athkar.sa.compose.primaryContainerDarkMediumContrast
import com.athkar.sa.compose.primaryContainerLight
import com.athkar.sa.compose.primaryContainerLightHighContrast
import com.athkar.sa.compose.primaryContainerLightMediumContrast
import com.athkar.sa.compose.primaryDark
import com.athkar.sa.compose.primaryDarkHighContrast
import com.athkar.sa.compose.primaryDarkMediumContrast
import com.athkar.sa.compose.primaryLight
import com.athkar.sa.compose.primaryLightHighContrast
import com.athkar.sa.compose.primaryLightMediumContrast
import com.athkar.sa.compose.scrimDark
import com.athkar.sa.compose.scrimDarkHighContrast
import com.athkar.sa.compose.scrimDarkMediumContrast
import com.athkar.sa.compose.scrimLight
import com.athkar.sa.compose.scrimLightHighContrast
import com.athkar.sa.compose.scrimLightMediumContrast
import com.athkar.sa.compose.secondaryContainerDark
import com.athkar.sa.compose.secondaryContainerDarkHighContrast
import com.athkar.sa.compose.secondaryContainerDarkMediumContrast
import com.athkar.sa.compose.secondaryContainerLight
import com.athkar.sa.compose.secondaryContainerLightHighContrast
import com.athkar.sa.compose.secondaryContainerLightMediumContrast
import com.athkar.sa.compose.secondaryDark
import com.athkar.sa.compose.secondaryDarkHighContrast
import com.athkar.sa.compose.secondaryDarkMediumContrast
import com.athkar.sa.compose.secondaryLight
import com.athkar.sa.compose.secondaryLightHighContrast
import com.athkar.sa.compose.secondaryLightMediumContrast
import com.athkar.sa.compose.surfaceBrightDark
import com.athkar.sa.compose.surfaceBrightDarkHighContrast
import com.athkar.sa.compose.surfaceBrightDarkMediumContrast
import com.athkar.sa.compose.surfaceBrightLight
import com.athkar.sa.compose.surfaceBrightLightHighContrast
import com.athkar.sa.compose.surfaceBrightLightMediumContrast
import com.athkar.sa.compose.surfaceContainerDark
import com.athkar.sa.compose.surfaceContainerDarkHighContrast
import com.athkar.sa.compose.surfaceContainerDarkMediumContrast
import com.athkar.sa.compose.surfaceContainerHighDark
import com.athkar.sa.compose.surfaceContainerHighDarkHighContrast
import com.athkar.sa.compose.surfaceContainerHighDarkMediumContrast
import com.athkar.sa.compose.surfaceContainerHighLight
import com.athkar.sa.compose.surfaceContainerHighLightHighContrast
import com.athkar.sa.compose.surfaceContainerHighLightMediumContrast
import com.athkar.sa.compose.surfaceContainerHighestDark
import com.athkar.sa.compose.surfaceContainerHighestDarkHighContrast
import com.athkar.sa.compose.surfaceContainerHighestDarkMediumContrast
import com.athkar.sa.compose.surfaceContainerHighestLight
import com.athkar.sa.compose.surfaceContainerHighestLightHighContrast
import com.athkar.sa.compose.surfaceContainerHighestLightMediumContrast
import com.athkar.sa.compose.surfaceContainerLight
import com.athkar.sa.compose.surfaceContainerLightHighContrast
import com.athkar.sa.compose.surfaceContainerLightMediumContrast
import com.athkar.sa.compose.surfaceContainerLowDark
import com.athkar.sa.compose.surfaceContainerLowDarkHighContrast
import com.athkar.sa.compose.surfaceContainerLowDarkMediumContrast
import com.athkar.sa.compose.surfaceContainerLowLight
import com.athkar.sa.compose.surfaceContainerLowLightHighContrast
import com.athkar.sa.compose.surfaceContainerLowLightMediumContrast
import com.athkar.sa.compose.surfaceContainerLowestDark
import com.athkar.sa.compose.surfaceContainerLowestDarkHighContrast
import com.athkar.sa.compose.surfaceContainerLowestDarkMediumContrast
import com.athkar.sa.compose.surfaceContainerLowestLight
import com.athkar.sa.compose.surfaceContainerLowestLightHighContrast
import com.athkar.sa.compose.surfaceContainerLowestLightMediumContrast
import com.athkar.sa.compose.surfaceDark
import com.athkar.sa.compose.surfaceDarkHighContrast
import com.athkar.sa.compose.surfaceDarkMediumContrast
import com.athkar.sa.compose.surfaceDimDark
import com.athkar.sa.compose.surfaceDimDarkHighContrast
import com.athkar.sa.compose.surfaceDimDarkMediumContrast
import com.athkar.sa.compose.surfaceDimLight
import com.athkar.sa.compose.surfaceDimLightHighContrast
import com.athkar.sa.compose.surfaceDimLightMediumContrast
import com.athkar.sa.compose.surfaceLight
import com.athkar.sa.compose.surfaceLightHighContrast
import com.athkar.sa.compose.surfaceLightMediumContrast
import com.athkar.sa.compose.surfaceVariantDark
import com.athkar.sa.compose.surfaceVariantDarkHighContrast
import com.athkar.sa.compose.surfaceVariantDarkMediumContrast
import com.athkar.sa.compose.surfaceVariantLight
import com.athkar.sa.compose.surfaceVariantLightHighContrast
import com.athkar.sa.compose.surfaceVariantLightMediumContrast
import com.athkar.sa.compose.tertiaryContainerDark
import com.athkar.sa.compose.tertiaryContainerDarkHighContrast
import com.athkar.sa.compose.tertiaryContainerDarkMediumContrast
import com.athkar.sa.compose.tertiaryContainerLight
import com.athkar.sa.compose.tertiaryContainerLightHighContrast
import com.athkar.sa.compose.tertiaryContainerLightMediumContrast
import com.athkar.sa.compose.tertiaryDark
import com.athkar.sa.compose.tertiaryDarkHighContrast
import com.athkar.sa.compose.tertiaryDarkMediumContrast
import com.athkar.sa.compose.tertiaryLight
import com.athkar.sa.compose.tertiaryLightHighContrast
import com.athkar.sa.compose.tertiaryLightMediumContrast


private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)

private val mediumContrastLightColorScheme = lightColorScheme(
    primary = primaryLightMediumContrast,
    onPrimary = onPrimaryLightMediumContrast,
    primaryContainer = primaryContainerLightMediumContrast,
    onPrimaryContainer = onPrimaryContainerLightMediumContrast,
    secondary = secondaryLightMediumContrast,
    onSecondary = onSecondaryLightMediumContrast,
    secondaryContainer = secondaryContainerLightMediumContrast,
    onSecondaryContainer = onSecondaryContainerLightMediumContrast,
    tertiary = tertiaryLightMediumContrast,
    onTertiary = onTertiaryLightMediumContrast,
    tertiaryContainer = tertiaryContainerLightMediumContrast,
    onTertiaryContainer = onTertiaryContainerLightMediumContrast,
    error = errorLightMediumContrast,
    onError = onErrorLightMediumContrast,
    errorContainer = errorContainerLightMediumContrast,
    onErrorContainer = onErrorContainerLightMediumContrast,
    background = backgroundLightMediumContrast,
    onBackground = onBackgroundLightMediumContrast,
    surface = surfaceLightMediumContrast,
    onSurface = onSurfaceLightMediumContrast,
    surfaceVariant = surfaceVariantLightMediumContrast,
    onSurfaceVariant = onSurfaceVariantLightMediumContrast,
    outline = outlineLightMediumContrast,
    outlineVariant = outlineVariantLightMediumContrast,
    scrim = scrimLightMediumContrast,
    inverseSurface = inverseSurfaceLightMediumContrast,
    inverseOnSurface = inverseOnSurfaceLightMediumContrast,
    inversePrimary = inversePrimaryLightMediumContrast,
    surfaceDim = surfaceDimLightMediumContrast,
    surfaceBright = surfaceBrightLightMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestLightMediumContrast,
    surfaceContainerLow = surfaceContainerLowLightMediumContrast,
    surfaceContainer = surfaceContainerLightMediumContrast,
    surfaceContainerHigh = surfaceContainerHighLightMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestLightMediumContrast,
)

private val highContrastLightColorScheme = lightColorScheme(
    primary = primaryLightHighContrast,
    onPrimary = onPrimaryLightHighContrast,
    primaryContainer = primaryContainerLightHighContrast,
    onPrimaryContainer = onPrimaryContainerLightHighContrast,
    secondary = secondaryLightHighContrast,
    onSecondary = onSecondaryLightHighContrast,
    secondaryContainer = secondaryContainerLightHighContrast,
    onSecondaryContainer = onSecondaryContainerLightHighContrast,
    tertiary = tertiaryLightHighContrast,
    onTertiary = onTertiaryLightHighContrast,
    tertiaryContainer = tertiaryContainerLightHighContrast,
    onTertiaryContainer = onTertiaryContainerLightHighContrast,
    error = errorLightHighContrast,
    onError = onErrorLightHighContrast,
    errorContainer = errorContainerLightHighContrast,
    onErrorContainer = onErrorContainerLightHighContrast,
    background = backgroundLightHighContrast,
    onBackground = onBackgroundLightHighContrast,
    surface = surfaceLightHighContrast,
    onSurface = onSurfaceLightHighContrast,
    surfaceVariant = surfaceVariantLightHighContrast,
    onSurfaceVariant = onSurfaceVariantLightHighContrast,
    outline = outlineLightHighContrast,
    outlineVariant = outlineVariantLightHighContrast,
    scrim = scrimLightHighContrast,
    inverseSurface = inverseSurfaceLightHighContrast,
    inverseOnSurface = inverseOnSurfaceLightHighContrast,
    inversePrimary = inversePrimaryLightHighContrast,
    surfaceDim = surfaceDimLightHighContrast,
    surfaceBright = surfaceBrightLightHighContrast,
    surfaceContainerLowest = surfaceContainerLowestLightHighContrast,
    surfaceContainerLow = surfaceContainerLowLightHighContrast,
    surfaceContainer = surfaceContainerLightHighContrast,
    surfaceContainerHigh = surfaceContainerHighLightHighContrast,
    surfaceContainerHighest = surfaceContainerHighestLightHighContrast,
)

private val mediumContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkMediumContrast,
    onPrimary = onPrimaryDarkMediumContrast,
    primaryContainer = primaryContainerDarkMediumContrast,
    onPrimaryContainer = onPrimaryContainerDarkMediumContrast,
    secondary = secondaryDarkMediumContrast,
    onSecondary = onSecondaryDarkMediumContrast,
    secondaryContainer = secondaryContainerDarkMediumContrast,
    onSecondaryContainer = onSecondaryContainerDarkMediumContrast,
    tertiary = tertiaryDarkMediumContrast,
    onTertiary = onTertiaryDarkMediumContrast,
    tertiaryContainer = tertiaryContainerDarkMediumContrast,
    onTertiaryContainer = onTertiaryContainerDarkMediumContrast,
    error = errorDarkMediumContrast,
    onError = onErrorDarkMediumContrast,
    errorContainer = errorContainerDarkMediumContrast,
    onErrorContainer = onErrorContainerDarkMediumContrast,
    background = backgroundDarkMediumContrast,
    onBackground = onBackgroundDarkMediumContrast,
    surface = surfaceDarkMediumContrast,
    onSurface = onSurfaceDarkMediumContrast,
    surfaceVariant = surfaceVariantDarkMediumContrast,
    onSurfaceVariant = onSurfaceVariantDarkMediumContrast,
    outline = outlineDarkMediumContrast,
    outlineVariant = outlineVariantDarkMediumContrast,
    scrim = scrimDarkMediumContrast,
    inverseSurface = inverseSurfaceDarkMediumContrast,
    inverseOnSurface = inverseOnSurfaceDarkMediumContrast,
    inversePrimary = inversePrimaryDarkMediumContrast,
    surfaceDim = surfaceDimDarkMediumContrast,
    surfaceBright = surfaceBrightDarkMediumContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkMediumContrast,
    surfaceContainerLow = surfaceContainerLowDarkMediumContrast,
    surfaceContainer = surfaceContainerDarkMediumContrast,
    surfaceContainerHigh = surfaceContainerHighDarkMediumContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkMediumContrast,
)

private val highContrastDarkColorScheme = darkColorScheme(
    primary = primaryDarkHighContrast,
    onPrimary = onPrimaryDarkHighContrast,
    primaryContainer = primaryContainerDarkHighContrast,
    onPrimaryContainer = onPrimaryContainerDarkHighContrast,
    secondary = secondaryDarkHighContrast,
    onSecondary = onSecondaryDarkHighContrast,
    secondaryContainer = secondaryContainerDarkHighContrast,
    onSecondaryContainer = onSecondaryContainerDarkHighContrast,
    tertiary = tertiaryDarkHighContrast,
    onTertiary = onTertiaryDarkHighContrast,
    tertiaryContainer = tertiaryContainerDarkHighContrast,
    onTertiaryContainer = onTertiaryContainerDarkHighContrast,
    error = errorDarkHighContrast,
    onError = onErrorDarkHighContrast,
    errorContainer = errorContainerDarkHighContrast,
    onErrorContainer = onErrorContainerDarkHighContrast,
    background = backgroundDarkHighContrast,
    onBackground = onBackgroundDarkHighContrast,
    surface = surfaceDarkHighContrast,
    onSurface = onSurfaceDarkHighContrast,
    surfaceVariant = surfaceVariantDarkHighContrast,
    onSurfaceVariant = onSurfaceVariantDarkHighContrast,
    outline = outlineDarkHighContrast,
    outlineVariant = outlineVariantDarkHighContrast,
    scrim = scrimDarkHighContrast,
    inverseSurface = inverseSurfaceDarkHighContrast,
    inverseOnSurface = inverseOnSurfaceDarkHighContrast,
    inversePrimary = inversePrimaryDarkHighContrast,
    surfaceDim = surfaceDimDarkHighContrast,
    surfaceBright = surfaceBrightDarkHighContrast,
    surfaceContainerLowest = surfaceContainerLowestDarkHighContrast,
    surfaceContainerLow = surfaceContainerLowDarkHighContrast,
    surfaceContainer = surfaceContainerDarkHighContrast,
    surfaceContainerHigh = surfaceContainerHighDarkHighContrast,
    surfaceContainerHighest = surfaceContainerHighestDarkHighContrast,
)

//val extendedLight = ExtendedColorScheme(
//)
//
//val extendedDark = ExtendedColorScheme(
//)
//
//val extendedLightMediumContrast = ExtendedColorScheme(
//)
//
//val extendedLightHighContrast = ExtendedColorScheme(
//)
//
//val extendedDarkMediumContrast = ExtendedColorScheme(
//)
//
//val extendedDarkHighContrast = ExtendedColorScheme(
//)

@Immutable
data class ColorFamily(
    val color: Color,
    val onColor: Color,
    val colorContainer: Color,
    val onColorContainer: Color
)

val unspecified_scheme = ColorFamily(
    Color.Unspecified, Color.Unspecified, Color.Unspecified, Color.Unspecified
)

@Composable
fun AthkarAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable() () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> darkScheme
        else -> lightScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
//        SideEffect {
//            val window = (view.context as Activity).window
////            window.statusBarColor = colorScheme.primary.toArgb()
////            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
//        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
