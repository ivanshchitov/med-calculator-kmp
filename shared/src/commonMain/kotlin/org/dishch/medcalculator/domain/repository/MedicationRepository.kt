package org.dishch.medcalculator.domain.repository

import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.data.local.DosageRegimenEntity
import org.dishch.medcalculator.data.local.MedicationEntity
import org.dishch.medcalculator.domain.model.Medication

interface MedicationRepository {
    fun getAllMedications(): Flow<List<MedicationEntity>>
    fun getRegimensByMedicationId(medicationId: String): Flow<List<DosageRegimenEntity>>
    fun getMedicationById(medicationId: String): Flow<Medication?>
}