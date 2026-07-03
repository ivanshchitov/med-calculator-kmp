package org.dishch.medcalculator.domain.repository

import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication

interface MedicationRepository {
    fun getAllMedications(): Flow<List<Medication>>
    fun getMedicationById(id: Long): Flow<Medication?>
    fun getRegimensForMedication(medicationId: Long): Flow<List<DosageRegimen>>
}