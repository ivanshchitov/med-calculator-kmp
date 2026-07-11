package org.dishch.medcalculator.data.local

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode

internal actual object LocaleHelper {

    actual fun getSystemLanguageCode(): String = NSLocale.currentLocale.languageCode ?: "en"
}