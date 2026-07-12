package org.dishch.medcalculator.domain.usecase

import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.model.CalculationResults
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication

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
        val regimen = dosageRegimens.find { ageInMonths in it.fromMonths..it.toMonths }
            ?: return null

        val minDoseMg = weight * (regimen.minDose ?: 0.0)
        val maxDoseMg = weight * (regimen.maxDose ?: 0.0)

        return CalculationResults(
            weight = weight,
            age = age,
            ageUnit = ageUnit,
            medication = medication,
            minDoseMg = minDoseMg,
            maxDoseMg = maxDoseMg,
            minVolMl = minDoseMg / medication.dosage,
            maxVolMl = maxDoseMg / medication.dosage,
            isMaxDailyDoseExceeded = maxDoseMg > (medication.maxSingleDose ?: 0.0)
        )
    }
}
