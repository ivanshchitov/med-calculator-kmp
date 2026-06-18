package org.dishch.medcalculator.ui.theme

import androidx.compose.runtime.Composable

@Composable
expect fun PlatformTheme(
    darkTheme: Boolean,
    content: @Composable () -> Unit
)
