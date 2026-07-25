package org.dishch.medcalculator

import kotlin.math.pow
import kotlin.math.roundToInt

fun Double.formatAsDecimal(scale: Int = 2): String {
    val factor = 10.0.pow(scale)
    val rounded = (this * factor).roundToInt() / factor
    return if (rounded % 1.0 == 0.0) {
        rounded.toLong().toString()
    } else {
        rounded.toString()
    }
}