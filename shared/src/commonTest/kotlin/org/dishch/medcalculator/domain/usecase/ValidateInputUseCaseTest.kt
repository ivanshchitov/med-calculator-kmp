package org.dishch.medcalculator.domain.usecase

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue
import org.dishch.medcalculator.domain.model.AgeUnit

class ValidateInputUseCaseTest {

    private val validateInputUseCase = ValidateInputUseCase()

    @Test
    fun testValidInput_WeightAndAge_Years() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "5",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(true, result.isValid)
        assertEquals(12.5, result.weight)
        assertEquals(5, result.age)
        assertEquals(AgeUnit.YEARS, result.ageUnit)
    }

    @Test
    fun testValidInput_WeightAndAge_Months() {
        val result = validateInputUseCase(
            weightString = "8.5",
            ageString = "6",
            ageUnit = AgeUnit.MONTHS
        )

        assertEquals(true, result.isValid)
        assertEquals(8.5, result.weight)
        assertEquals(6, result.age)
        assertEquals(AgeUnit.MONTHS, result.ageUnit)
    }

    @Test
    fun testInvalidWeight_TooLow() {
        val result = validateInputUseCase(
            weightString = "0.5",
            ageString = "5",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(false, result.isValid)
        assertNull(result.weight)
    }

    @Test
    fun testInvalidWeight_TooHigh() {
        val result = validateInputUseCase(
            weightString = "150.0",
            ageString = "5",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(false, result.isValid)
        assertNull(result.weight)
    }

    @Test
    fun testInvalidAge_Age_Years() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "20",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(false, result.isValid)
        assertNull(result.age)
    }

    @Test
    fun testInvalidAge_Age_Months() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "15",
            ageUnit = AgeUnit.MONTHS
        )

        assertEquals(false, result.isValid)
        assertNull(result.age)
    }

    @Test
    fun testInvalidWeight_Text() {
        val result = validateInputUseCase(
            weightString = "abc",
            ageString = "5",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(false, result.isValid)
        assertNull(result.weight)
    }

    @Test
    fun testInvalidAge_Text() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "abc",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(false, result.isValid)
        assertNull(result.age)
    }

    @Test
    fun testEmptyWeight() {
        val result = validateInputUseCase(
            weightString = "",
            ageString = "5",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(false, result.isValid)
        assertNull(result.weight)
    }

    @Test
    fun testEmptyAge() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(false, result.isValid)
        assertNull(result.age)
    }

    @Test
    fun testNullAgeUnit() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "5",
            ageUnit = null
        )

        assertEquals(false, result.isValid)
    }

    @Test
    fun testBoundaryWeight_Min() {
        val result = validateInputUseCase(
            weightString = "1.0",
            ageString = "5",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(true, result.isValid)
        assertEquals(1.0, result.weight)
    }

    @Test
    fun testBoundaryWeight_Max() {
        val result = validateInputUseCase(
            weightString = "100.0",
            ageString = "5",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(true, result.isValid)
        assertEquals(100.0, result.weight)
    }

    @Test
    fun testBoundaryAge_Years_Min() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "1",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(true, result.isValid)
        assertEquals(1, result.age)
    }

    @Test
    fun testBoundaryAge_Years_Max() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "17",
            ageUnit = AgeUnit.YEARS
        )

        assertEquals(true, result.isValid)
        assertEquals(17, result.age)
    }

    @Test
    fun testBoundaryAge_Months_Min() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "1",
            ageUnit = AgeUnit.MONTHS
        )

        assertEquals(true, result.isValid)
        assertEquals(1, result.age)
    }

    @Test
    fun testBoundaryAge_Months_Max() {
        val result = validateInputUseCase(
            weightString = "12.5",
            ageString = "11",
            ageUnit = AgeUnit.MONTHS
        )

        assertEquals(true, result.isValid)
        assertEquals(11, result.age)
    }
}
