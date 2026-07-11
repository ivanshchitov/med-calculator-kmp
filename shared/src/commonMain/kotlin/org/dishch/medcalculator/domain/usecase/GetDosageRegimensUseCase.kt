package org.dishch.medcalculator.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.repository.MedicationFullRepository

class GetDosageRegimensUseCase(private val repository: MedicationFullRepository) {
    operator fun invoke(medicationId: String): Flow<List<DosageRegimen>> {
        return repository.getRegimensByMedicationId(medicationId).map { entities ->
            entities.map { entity ->
                DosageRegimen(
                    id = entity.id,
                    fromAge = entity.fromMonths,
                    toAge = entity.toMonths,
                    minDosePerKg = entity.doseMin ?: 0.0,
                    maxDosePerKg = entity.doseMax ?: 0.0,
                    medicationId = entity.medicationId
                )
            }
        }
    }
}
