package org.dishch.medcalculator

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import kotlinx.browser.document
import org.dishch.medcalculator.di.initKoin
import org.w3c.dom.HTMLElement

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    try {
        initKoin { }
    } catch (error: Throwable) {
        println("INIT FAILED: ${error.stackTraceToString()}")
        throw error
    }

    ComposeViewport(document.getElementById("app") as HTMLElement) {
        App()
    }
}
