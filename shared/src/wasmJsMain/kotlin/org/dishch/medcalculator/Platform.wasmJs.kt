package org.dishch.medcalculator

import kotlinx.browser.window

class WasmPlatform : Platform {
    override val name: String = "Web (WasmJS)"
}

actual fun getPlatform(): Platform = WasmPlatform()

actual fun needsManualKeyboardScroll(): Boolean {
    val navigator = window.navigator
    val userAgent = navigator.userAgent
    val isIPad = navigator.platform == "MacIntel" && navigator.maxTouchPoints > 1
    return userAgent.contains("iPhone", ignoreCase = true) ||
        userAgent.contains("iPad", ignoreCase = true) ||
        userAgent.contains("iPod", ignoreCase = true) ||
        isIPad
}
