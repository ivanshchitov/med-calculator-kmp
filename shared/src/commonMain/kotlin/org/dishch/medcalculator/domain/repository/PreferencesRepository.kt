package org.dishch.medcalculator.domain.repository

import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.domain.model.AgeUnit

interface PreferencesRepository {
    val weight: Flow<Double>
    val age: Flow<Int>
    val ageUnit: Flow<AgeUnit>
    val medicationId: Flow<Long>

    suspend fun update(weight: Double, age: Int, ageUnit: AgeUnit, medId: Long)
}
