package org.dishch.medcalculator.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.dishch.medcalculator.data.local.toDomain
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.repository.MedicationFullRepository

class GetMedicationByIdUseCase(private val repository: MedicationFullRepository) {

    operator fun invoke(medicationId: String): Flow<Medication?> =
        repository.getMedicationById(medicationId).map { it?.toDomain() }
}