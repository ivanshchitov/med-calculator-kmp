package org.dishch.medcalculator.domain

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.age_limit_months_plurals
import medcalculator.shared.generated.resources.age_limit_years_plurals
import org.jetbrains.compose.resources.pluralStringResource

sealed class AgeLimit {
    data class Years(val count: Int) : AgeLimit()
    data class Months(val count: Int) : AgeLimit()
}

@Serializable
data class Medication(
    val id: Long,
    val name: String,
    val dosage: Double,
    val maxSingleDose: Double,
    val ageLimit: Int
)

val Medication.formattedDosage: String
    get() = if (dosage % 1.0 == 0.0) {
        dosage.toInt().toString()
    } else {
        dosage.toString()
    }

val Medication.formattedMaxSingleDose: String
    get() = if (dosage % 1.0 == 0.0) {
        dosage.toInt().toString()
    } else {
        dosage.toString()
    }

val Medication.ageLimitData: AgeLimit
    get() = if (ageLimit == 0 || ageLimit >= 12) {
        AgeLimit.Years(ageLimit / 12)
    } else {
        AgeLimit.Months(ageLimit)
    }

@Composable
fun AgeLimit.toDisplayString(): String = when (this) {
    is AgeLimit.Years -> pluralStringResource(Res.plurals.age_limit_years_plurals, count, count)
    is AgeLimit.Months -> pluralStringResource(Res.plurals.age_limit_months_plurals, count, count)
}