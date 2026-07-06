package org.dishch.medcalculator.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication

class CalculationUseCaseTest {

    private val calculationUseCase = CalculationUseCase()

    @Test
    fun testSuccessfulCalculation() {
        val medication = Medication(
            id = 1,
            name = "Test Med",
            dosage = 10.0,
            maxSingleDose = 500.0,
            ageLimit = 18
        )

        val regimens = listOf(
            DosageRegimen(
                id = 1,
                fromAge = 0,
                toAge = 216,
                minDosePerKg = 10.0,
                maxDosePerKg = 15.0,
                medicationId = 1
            )
        )

        val result = calculationUseCase(
            weight = 12.5,
            age = 5,
            ageUnit = AgeUnit.YEARS,
            selectedMedication = medication,
            dosageRegimens = regimens
        )

        assertNotNull(result)
        assertEquals(12.5, result.weight)
        assertEquals(5, result.age)
        assertEquals(AgeUnit.YEARS, result.ageUnit)
        assertEquals(medication, result.medication)
        assertEquals(125.0, result.minDoseMg) // 12.5 * 10
        assertEquals(187.5, result.maxDoseMg) // 12.5 * 15
        assertEquals(12.5, result.minVolMl) // 125.0 / 10
        assertEquals(18.75, result.maxVolMl) // 187.5 / 10
        assertEquals(false, result.isMaxDailyDoseExceeded)
    }

    @Test
    fun testNullMedication() {
        val result = calculationUseCase(
            weight = 12.5,
            age = 5,
            ageUnit = AgeUnit.YEARS,
            selectedMedication = null,
            dosageRegimens = emptyList()
        )

        assertNull(result)
    }

    @Test
    fun testInvalidMedicationDosage() {
        val medication = Medication(
            id = 1,
            name = "Test Med",
            dosage = 0.0,
            maxSingleDose = 500.0,
            ageLimit = 18
        )

        val result = calculationUseCase(
            weight = 12.5,
            age = 5,
            ageUnit = AgeUnit.YEARS,
            selectedMedication = medication,
            dosageRegimens = emptyList()
        )

        assertNull(result)
    }

    @Test
    fun testNoDosageRegimens() {
        val medication = Medication(
            id = 1,
            name = "Test Med",
            dosage = 10.0,
            maxSingleDose = 500.0,
            ageLimit = 18
        )

        val result = calculationUseCase(
            weight = 12.5,
            age = 5,
            ageUnit = AgeUnit.YEARS,
            selectedMedication = medication,
            dosageRegimens = emptyList()
        )

        assertNull(result)
    }

    @Test
    fun testNoMatchingRegimen() {
        val medication = Medication(
            id = 1,
            name = "Test Med",
            dosage = 10.0,
            maxSingleDose = 500.0,
            ageLimit = 18
        )

        val regimens = listOf(
            DosageRegimen(
                id = 1,
                fromAge = 100,
                toAge = 200,
                minDosePerKg = 10.0,
                maxDosePerKg = 15.0,
                medicationId = 1
            )
        )

        val result = calculationUseCase(
            weight = 12.5,
            age = 5,
            ageUnit = AgeUnit.YEARS,
            selectedMedication = medication,
            dosageRegimens = regimens
        )

        assertNull(result)
    }

    @Test
    fun testMaxDoseExceeded() {
        val medication = Medication(
            id = 1,
            name = "Test Med",
            dosage = 10.0,
            maxSingleDose = 100.0,
            ageLimit = 18
        )

        val regimens = listOf(
            DosageRegimen(
                id = 1,
                fromAge = 0,
                toAge = 216,
                minDosePerKg = 10.0,
                maxDosePerKg = 15.0,
                medicationId = 1
            )
        )

        val result = calculationUseCase(
            weight = 12.5,
            age = 5,
            ageUnit = AgeUnit.YEARS,
            selectedMedication = medication,
            dosageRegimens = regimens
        )

        assertNotNull(result)
        assertEquals(true, result.isMaxDailyDoseExceeded)
    }

    @Test
    fun testCalculationWithMonths() {
        val medication = Medication(
            id = 1,
            name = "Test Med",
            dosage = 10.0,
            maxSingleDose = 500.0,
            ageLimit = 18
        )

        val regimens = listOf(
            DosageRegimen(
                id = 1,
                fromAge = 0,
                toAge = 11,
                minDosePerKg = 10.0,
                maxDosePerKg = 15.0,
                medicationId = 1
            )
        )

        val result = calculationUseCase(
            weight = 6.0,
            age = 6,
            ageUnit = AgeUnit.MONTHS,
            selectedMedication = medication,
            dosageRegimens = regimens
        )

        assertNotNull(result)
        assertEquals(60.0, result.minDoseMg) // 6.0 * 10
        assertEquals(90.0, result.maxDoseMg) // 6.0 * 15
    }

    @Test
    fun testMedicationWithZeroDosage() {
        val medication = Medication(
            id = 1,
            name = "Test Med",
            dosage = 0.0,
            maxSingleDose = 500.0,
            ageLimit = 18
        )

        val result = calculationUseCase(
            weight = 12.5,
            age = 5,
            ageUnit = AgeUnit.YEARS,
            selectedMedication = medication,
            dosageRegimens = emptyList()
        )

        assertNull(result)
    }
}
