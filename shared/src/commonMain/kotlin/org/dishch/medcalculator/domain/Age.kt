package org.dishch.medcalculator.domain

import androidx.compose.runtime.Composable
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.age_limit_months_plurals
import medcalculator.shared.generated.resources.age_limit_years_plurals
import medcalculator.shared.generated.resources.age_months
import medcalculator.shared.generated.resources.age_years
import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.pluralStringResource

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

val Age.formattedAgeLimit: String
    @Composable
    get() = if (isYears || quantity == 0) {
        pluralStringResource(Res.plurals.age_limit_years_plurals, quantity, quantity)
    } else {
        pluralStringResource(Res.plurals.age_limit_months_plurals, quantity, quantity)
    }

@Composable
fun Age.toPluralsString(): String =
    pluralStringResource(pluralRes, quantity, quantity)

private val Age.pluralRes: PluralStringResource
    get() = if (isYears) Res.plurals.age_years else Res.plurals.age_months
