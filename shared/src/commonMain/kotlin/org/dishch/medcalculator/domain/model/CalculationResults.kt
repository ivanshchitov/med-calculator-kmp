package org.dishch.medcalculator.domain.model

import kotlinx.serialization.Serializable
import org.dishch.medcalculator.formatAsDecimal

@Serializable
data class CalculationResults(
    val weight: Double,
    val age: Int,
    val ageUnit: AgeUnit,
    val medication: MedicationInfo,
    val resultsByRoute: Map<Route, RouteCalculationResults>
)

@Serializable
data class MedicationInfo(
    val name: String,
    val formattedDosage: String
)

@Serializable
data class RouteCalculationResults(
    val minDoseMg: Double,
    val maxDoseMg: Double,
    val minVolMl: Double,
    val maxVolMl: Double,
    val isMaxDailyDoseExceeded: Boolean
)

val RouteCalculationResults.formattedDoseRange: String
    get() = if (minDoseMg == maxDoseMg) {
        minDoseMg.formatAsDecimal()
    } else {
        "${minDoseMg.formatAsDecimal()}-${maxDoseMg.formatAsDecimal()}"
    }

val RouteCalculationResults.formattedVolumeRange: String
    get() = if (minVolMl == maxVolMl) {
        minVolMl.formatAsDecimal()
    } else {
        "${minVolMl.formatAsDecimal()}-${maxVolMl.formatAsDecimal()}"
    }
