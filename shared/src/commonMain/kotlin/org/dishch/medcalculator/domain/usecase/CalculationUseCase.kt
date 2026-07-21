package org.dishch.medcalculator.domain.usecase

import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.model.CalculationResults
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.DosageUnit
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.model.RouteCalculationResults

class CalculationUseCase {

    operator fun invoke(
        weight: Double,
        age: Int,
        ageUnit: AgeUnit,
        selectedMedication: Medication?,
        dosageRegimens: List<DosageRegimen>
    ): CalculationResults? {
        return validateInputAndCalculate(
            weight = weight,
            age = age,
            ageUnit = ageUnit,
            medication = selectedMedication,
            dosageRegimens = dosageRegimens
        )
    }

    private fun validateInputAndCalculate(
        weight: Double,
        age: Int,
        ageUnit: AgeUnit,
        medication: Medication?,
        dosageRegimens: List<DosageRegimen>
    ): CalculationResults? {
        if (medication == null) return null
        if (medication.dosage <= 0.0) return null
        if (dosageRegimens.isEmpty()) return null

        val ageInMonths = if (ageUnit == AgeUnit.YEARS) age * 12 else age

        val regimens = dosageRegimens.filter {
            if (it.fromKg != null && it.toKg != null) {
                weight in it.fromKg..it.toKg
            } else {
                ageInMonths in it.fromMonths..it.toMonths
            }
        } ?: return null

        val resultsByRoute = regimens.associate { regimen ->
            val contraindicated = regimen.minDose == null || regimen.maxDose == null
            val minDoseMg = if (regimen.dosageUnit == DosageUnit.MG)
                regimen.minDose ?: 0.0
            else weight * (regimen.minDose ?: 0.0)
            val maxDoseMg = if (regimen.dosageUnit == DosageUnit.MG)
                regimen.maxDose ?: 0.0
            else weight * (regimen.maxDose ?: 0.0)

            val concentration = medication.dosage.takeIf { it > 0.0 } ?: 1.0
            val maxSingleDose = regimen.maxDoseMg ?: medication.maxSingleDose

            regimen.route to RouteCalculationResults(
                minDoseMg = minDoseMg,
                maxDoseMg = maxDoseMg,
                minVolMl = minDoseMg / concentration,
                maxVolMl = maxDoseMg / concentration,
                contraindicated = contraindicated,
                maxSingleDose = maxSingleDose,
                isMaxDailyDoseExceeded = maxSingleDose != null && maxDoseMg > maxSingleDose
//                    (regimen.maxDoseMg != null && maxDoseMg > (regimen.maxDoseMg))
//                            || (medication.maxSingleDose != null && maxDoseMg > medication.maxSingleDose)
            )
        }

        return CalculationResults(
            weight = weight,
            age = age,
            ageUnit = ageUnit,
            medication = medication,
            dosageRegimens = dosageRegimens,
            resultsByRoute = resultsByRoute
        )
    }
}
