package org.dishch.medcalculator.domain

import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    fun getAllMedications(): Flow<List<Medication>>
    fun getMedicationById(id: Long): Flow<Medication?>
    fun getRegimensForMedication(medicationId: Long): Flow<List<DosageRegimen>>
}
