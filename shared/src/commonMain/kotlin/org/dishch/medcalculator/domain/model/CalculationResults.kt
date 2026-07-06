package org.dishch.medcalculator.domain.model

import kotlinx.serialization.Serializable
import org.dishch.medcalculator.formatAsDecimal

@Serializable
data class CalculationResults(
    val weight: Double,
    val age: Int,
    val ageUnit: AgeUnit,
    val medication: Medication,
    val minDoseMg: Double,
    val maxDoseMg: Double,
    val minVolMl: Double,
    val maxVolMl: Double,
    val isMaxDailyDoseExceeded: Boolean
)

val CalculationResults.formattedDoseRange: String
    get() = if (minDoseMg == maxDoseMg) {
        minDoseMg.formatAsDecimal()
    } else {
        "${minDoseMg.formatAsDecimal()}-${maxDoseMg.formatAsDecimal()}"
    }

val CalculationResults.formattedVolumeRange: String
    get() = if (minVolMl == maxVolMl) {
        minVolMl.formatAsDecimal()
    } else {
        "${minVolMl.formatAsDecimal()}-${maxVolMl.formatAsDecimal()}"
    }
