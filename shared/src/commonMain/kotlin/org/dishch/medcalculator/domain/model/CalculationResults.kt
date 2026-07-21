package org.dishch.medcalculator.domain.model

import androidx.compose.runtime.Composable
import kotlinx.serialization.Serializable
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.mg_format
import org.dishch.medcalculator.formatAsDecimal
import org.jetbrains.compose.resources.stringResource

@Serializable
data class CalculationResults(
    val weight: Double,
    val age: Int,
    val ageUnit: AgeUnit,
    val medication: Medication,
    val dosageRegimens: List<DosageRegimen>,
    val resultsByRoute: Map<Route, RouteCalculationResults>
)

@Serializable
data class RouteCalculationResults(
    val minDoseMg: Double,
    val maxDoseMg: Double,
    val minVolMl: Double,
    val maxVolMl: Double,
    val maxSingleDose: Double?,
    val contraindicated: Boolean,
    val isMaxDailyDoseExceeded: Boolean,
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

val RouteCalculationResults.formattedMaxSingleDose: String
    @Composable
    get() = stringResource(Res.string.mg_format, maxSingleDose?.formatAsDecimal() ?: "")
