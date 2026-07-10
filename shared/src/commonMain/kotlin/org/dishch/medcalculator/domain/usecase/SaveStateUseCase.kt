package org.dishch.medcalculator.domain.usecase

import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.repository.PreferencesRepository

class SaveStateUseCase(
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(
        weight: Double,
        age: Int,
        ageUnit: AgeUnit,
        medicationId: Long
    ) {
        preferencesRepository.update(weight, age, ageUnit, medicationId)
    }
}
