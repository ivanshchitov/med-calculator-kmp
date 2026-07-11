package org.dishch.medcalculator.data.local

import java.util.Locale

internal actual object LocaleHelper {

    actual fun getSystemLanguageCode(): String = Locale.getDefault().language
}