package org.dishch.medcalculator

import kotlin.math.roundToInt

fun Double.formatAsDecimal(): String {
    val rounded = (this * 1000).roundToInt() / 1000.0
    return if (rounded % 1.0 == 0.0) {
        rounded.toLong().toString()
    } else {
        rounded.toString()
    }
}
