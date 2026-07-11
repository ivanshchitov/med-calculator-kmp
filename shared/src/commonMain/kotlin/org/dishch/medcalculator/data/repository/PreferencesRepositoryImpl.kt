package org.dishch.medcalculator.data.repository

import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.data.PreferenceManager
import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.repository.PreferencesRepository

class PreferencesRepositoryImpl(
    private val preferenceManager: PreferenceManager
) : PreferencesRepository {

    override val weight: Flow<Double> = preferenceManager.weight
    override val age: Flow<Int> = preferenceManager.age
    override val ageUnit: Flow<AgeUnit> = preferenceManager.ageUnit
    override val medicationId: Flow<String> = preferenceManager.medicationId

    override suspend fun update(
        weight: Double,
        age: Int,
        ageUnit: AgeUnit,
        medId: String
    ) {
        preferenceManager.updatePreferences(weight, age, ageUnit, medId)
    }
}
