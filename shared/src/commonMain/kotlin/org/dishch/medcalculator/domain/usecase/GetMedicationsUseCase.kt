package org.dishch.medcalculator.domain.usecase

import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.repository.MedicationRepository

class GetMedicationsUseCase(private val repository: MedicationRepository) {

    operator fun invoke(): Flow<List<Medication>> =
        repository.getAllMedications()
}
