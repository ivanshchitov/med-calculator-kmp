package org.dishch.medcalculator.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.repository.MedicationRepository

class GetDosageRegimensUseCase(private val repository: MedicationRepository) {

    operator fun invoke(medicationId: String): Flow<List<DosageRegimen>> =
        repository.getRegimensByMedicationId(medicationId)
}
