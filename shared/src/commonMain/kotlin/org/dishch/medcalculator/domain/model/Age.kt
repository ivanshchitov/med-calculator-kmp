package org.dishch.medcalculator.domain.model

private const val MONTHS_IN_YEAR = 12
const val ADULT_AGE_MONTHS = 216

data class Age(
    val quantity: Int,
    val isYears: Boolean
)

fun Int.toAge(): Age =
    if (this >= MONTHS_IN_YEAR) {
        Age(this / MONTHS_IN_YEAR, isYears = true)
    } else {
        Age(this, isYears = false)
    }
