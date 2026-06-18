package org.dishch.medcalculator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@Composable
fun MedCalculatorAppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = lightColorScheme(
        primary = AppColors.Primary,
        background = AppColors.Background,
        surface = AppColors.Surface
    )

    PlatformTheme(darkTheme = darkTheme) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content
        )
    }
}
