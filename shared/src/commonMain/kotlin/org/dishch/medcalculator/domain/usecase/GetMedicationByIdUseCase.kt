package org.dishch.medcalculator.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.repository.MedicationRepository

class GetMedicationByIdUseCase(private val repository: MedicationRepository) {

    operator fun invoke(medicationId: String): Flow<Medication?> =
        repository.getMedicationById(medicationId)
}