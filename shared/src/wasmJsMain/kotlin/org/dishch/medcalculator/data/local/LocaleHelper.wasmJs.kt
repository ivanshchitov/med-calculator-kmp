package org.dishch.medcalculator.data.local

import kotlinx.browser.window

internal actual object LocaleHelper {

    actual fun getSystemLanguageCode(): String =
        window.navigator.language.substringBefore('-').lowercase()
}
