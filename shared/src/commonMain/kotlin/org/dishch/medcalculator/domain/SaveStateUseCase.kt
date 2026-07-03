package org.dishch.medcalculator.domain

import org.dishch.medcalculator.data.PreferenceManager

class SaveStateUseCase(
    private val preferenceManager: PreferenceManager
) {

    suspend operator fun invoke(
        weight: Double,
        age: Int,
        ageUnit: AgeUnit,
        medicationId: Long
    ) {
        preferenceManager.save(
            weight = weight,
            age = age,
            ageUnit = ageUnit,
            medicationId = medicationId
        )
    }
}
