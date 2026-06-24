package org.dishch.medcalculator.domain.calculation

import org.dishch.medcalculator.domain.AgeUnit
import org.dishch.medcalculator.domain.CalculationResults
import org.dishch.medcalculator.domain.DosageRegimen
import org.dishch.medcalculator.domain.Medication

class CalculationUseCase {

    operator fun invoke(
        weight: String,
        age: String,
        ageUnit: AgeUnit,
        selectedMedication: Medication?,
        dosageRegimens: List<DosageRegimen>
    ): CalculationResults? {
        val weightDouble = weight.toDoubleOrNull() ?: return null
        val ageInt = age.toIntOrNull() ?: return null
        val medication = selectedMedication ?: return null

        if (medication.dosage <= 0.0)
            return null

        val ageInMonths = if (ageUnit == AgeUnit.YEARS) ageInt * 12 else ageInt
        val regimen = dosageRegimens.find { ageInMonths in it.fromAge..it.toAge }
            ?: return null

        val minDoseMg = weightDouble * regimen.minDosePerKg
        val maxDoseMg = weightDouble * regimen.maxDosePerKg

        return CalculationResults(
            weight = weightDouble,
            age = ageInt,
            ageUnit = ageUnit,
            medication = medication,
            minDoseMg = minDoseMg,
            maxDoseMg = maxDoseMg,
            minVolMl = minDoseMg / medication.dosage,
            maxVolMl = maxDoseMg / medication.dosage,
            isMaxDailyDoseExceeded = maxDoseMg > medication.maxSingleDose
        )
    }
}