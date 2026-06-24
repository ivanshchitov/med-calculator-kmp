package org.dishch.medcalculator.domain

import androidx.compose.runtime.Composable
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.age_range_format
import org.dishch.medcalculator.formattedDouble
import org.jetbrains.compose.resources.stringResource

data class DosageRegimen(
    val id: Long,
    val fromAge: Int,
    val toAge: Int,
    val minDosePerKg: Double,
    val maxDosePerKg: Double,
    val medicationId: Long
)

val DosageRegimen.formattedMinDose: String
    get() = formattedDouble(minDosePerKg)

val DosageRegimen.formattedMaxDose: String
    get() = formattedDouble(maxDosePerKg)

val DosageRegimen.formattedDoseRange: String
    get() = "$formattedMinDose-$formattedMaxDose"

val DosageRegimen.formattedAgeRange: String
    @Composable
    get() = when {
        toAge >= ADULT_AGE_MONTHS -> fromAge.toAge().formattedAgeLimit
        else -> {
            var startAgeString = fromAge.toAge().quantity.toString()
            if (fromAge.toAge().isYears != toAge.toAge().isYears) {
                startAgeString = fromAge.toAge().toPluralsString()
            }
            stringResource(Res.string.age_range_format,
                startAgeString,
                toAge.toAge().toPluralsString()
            )
        }
    }
