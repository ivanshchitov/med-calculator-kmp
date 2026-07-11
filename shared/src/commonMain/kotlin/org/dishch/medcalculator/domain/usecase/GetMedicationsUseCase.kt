package org.dishch.medcalculator.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.repository.MedicationFullRepository

class GetMedicationsUseCase(private val repository: MedicationFullRepository) {
    operator fun invoke(): Flow<List<Medication>> {
        return repository.getAllMedications().map { entities ->
            entities.map { entity ->
                Medication(
                    id = entity.id,
                    name = entity.nameRu,
                    dosage = entity.dosageMgPerMl,
                    maxSingleDose = entity.maxSingleDoseMg ?: 0.0,
                    ageLimit = entity.minAgeMonths
                )
            }
        }
    }
}
