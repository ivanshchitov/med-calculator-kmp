package org.dishch.medcalculator.domain.usecase

import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.ui.helpers.isYears
import org.dishch.medcalculator.ui.helpers.toAge

data class ValidationState(
    val isValid: Boolean,
    val weight: Double?,
    val age: Int?,
    val ageUnit: AgeUnit?
)

class ValidateInputUseCase {

    operator fun invoke(
        weightString: String,
        ageString: String,
        ageUnit: AgeUnit?,
        minMonths: Int,
        minWeight: Double?
    ): ValidationState {
        val weight = parseAndValidateWeight(weightString, minWeight)
        val age = parseAndValidateAge(ageString, ageUnit, minMonths)
        val isValid = weight != null && age != null

        return ValidationState(
            isValid = isValid,
            weight = weight,
            age = age,
            ageUnit = ageUnit
        )
    }

    private fun parseAndValidateWeight(value: String, minWeight: Double?): Double? {
        val weight = value.toDoubleOrNull()
        return if (weight != null && weight in (minWeight?.let { it } ?: 1.0)..100.0) weight else null
    }

    private fun parseAndValidateAge(value: String, ageUnit: AgeUnit?, minAge: Int): Int? {
        val age = value.toIntOrNull()
        val isValidAge = when (ageUnit) {
            AgeUnit.MONTHS -> age != null && age in minAge..11
            AgeUnit.YEARS if minAge.isYears() -> age != null && age in minAge.toAge()..17
            AgeUnit.YEARS if !minAge.isYears() -> age != null && age in 1..17
            else -> false
        }
        return if (isValidAge) age else null
    }
}
