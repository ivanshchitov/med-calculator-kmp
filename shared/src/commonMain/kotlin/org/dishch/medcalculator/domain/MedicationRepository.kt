package org.dishch.medcalculator.domain

import kotlinx.coroutines.flow.Flow

interface MedicationRepository {
    fun getAllMedications(): Flow<List<Medication>>
    suspend fun getMedicationById(id: Long): Medication?
    fun getRegimensForMedication(medicationId: Long): Flow<List<DosageRegimen>>
}
