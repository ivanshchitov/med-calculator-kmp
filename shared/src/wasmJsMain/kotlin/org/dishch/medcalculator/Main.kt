package org.dishch.medcalculator

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import org.dishch.medcalculator.di.initKoin

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    initKoin { }
    ComposeViewport {
        App()
    }
}
