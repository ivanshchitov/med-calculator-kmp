package org.dishch.medcalculator

import org.dishch.medcalculator.domain.AgeUnit
import kotlin.math.roundToInt

fun Double.formatAsDecimal(): String {
    val rounded = (this * 100).roundToInt() / 100.0
    return if (rounded % 1.0 == 0.0) {
        rounded.toLong().toString()
    } else {
        rounded.toString()
    }
}

fun isAgeValid(age: Int?, ageUnit: AgeUnit) =
    when (ageUnit) {
        AgeUnit.MONTHS -> age != null && age in 1..11
        AgeUnit.YEARS -> age != null && age in 1..17
    }

fun isWeightValid(weight: Double?) =
    weight != null && weight in 1.0..100.0