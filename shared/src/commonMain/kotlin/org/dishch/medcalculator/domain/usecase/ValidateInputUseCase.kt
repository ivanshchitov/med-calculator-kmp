package org.dishch.medcalculator.domain.usecase

import org.dishch.medcalculator.domain.model.AgeUnit

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
        ageUnit: AgeUnit?
    ): ValidationState {
        val weight = parseAndValidateWeight(weightString)
        val age = parseAndValidateAge(ageString, ageUnit)
        val isValid = weight != null && age != null

        return ValidationState(
            isValid = isValid,
            weight = weight,
            age = age,
            ageUnit = ageUnit
        )
    }

    private fun parseAndValidateWeight(value: String): Double? {
        val weight = value.toDoubleOrNull()
        return if (weight != null && weight in 1.0..100.0) weight else null
    }

    private fun parseAndValidateAge(value: String, ageUnit: AgeUnit?): Int? {
        val age = value.toIntOrNull()
        val isValidAge = when (ageUnit) {
            AgeUnit.MONTHS -> age != null && age in 1..11
            AgeUnit.YEARS -> age != null && age in 1..17
            null -> false
        }
        return if (isValidAge) age else null
    }
}
