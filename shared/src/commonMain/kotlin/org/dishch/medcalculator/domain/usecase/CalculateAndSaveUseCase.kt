package org.dishch.medcalculator.domain.usecase

import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.model.CalculationResults
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication

class CalculateAndSaveUseCase(
    private val calculationUseCase: CalculationUseCase,
    private val saveStateUseCase: SaveStateUseCase
) {
    suspend operator fun invoke(
        weight: Double,
        age: Int,
        ageUnit: AgeUnit,
        medication: Medication?,
        dosageRegimens: List<DosageRegimen>
    ): CalculationResults? {
        val result = calculationUseCase(weight, age, ageUnit, medication, dosageRegimens)
        if (result != null) {
            saveStateUseCase(weight, age, ageUnit, medication?.id ?: 1L)
        }
        return result
    }
}
