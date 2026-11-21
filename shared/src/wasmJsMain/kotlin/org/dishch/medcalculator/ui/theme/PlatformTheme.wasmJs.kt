package org.dishch.medcalculator.ui.theme

import androidx.compose.runtime.Composable

@Composable
actual fun PlatformTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
) {
    content()
}
