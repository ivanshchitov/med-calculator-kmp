package org.dishch.medcalculator

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

/**
 * iOS Safari does not update Compose's scroll viewport before the keyboard animation.
 * Android Chrome and native targets handle this themselves.
 */
expect fun needsManualKeyboardScroll(): Boolean