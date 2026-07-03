package org.dishch.medcalculator.ui

import org.dishch.medcalculator.domain.AgeUnit

object InputValidator {

    fun validateAge(age: String, unit: AgeUnit): Boolean {
        val ageInt = age.toIntOrNull()
        return isAgeValid(ageInt, unit)
    }

    fun validateWeight(weight: String): Boolean {
        val weightDouble = weight.toDoubleOrNull()
        return isWeightValid(weightDouble)
    }

    fun isAgeInputValid(value: String): Boolean {
        return value.isEmpty() || (value.all { it.isDigit() } && value.length <= 2)
    }

    fun isWeightInputValid(value: String): Boolean {
        return value.isEmpty() || (value.matches(Regex("""^\d*\.?\d{0,1}$""")) && value.length <= 4)
    }

    private fun isAgeValid(age: Int?, ageUnit: AgeUnit) =
        when (ageUnit) {
            AgeUnit.MONTHS -> age != null && age in 1..11
            AgeUnit.YEARS -> age != null && age in 1..17
        }

    private fun isWeightValid(weight: Double?) =
        weight != null && weight in 1.0..100.0
}
