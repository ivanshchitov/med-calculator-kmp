package org.dishch.medcalculator.domain.repository

import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.data.local.DosageRegimenFullEntity
import org.dishch.medcalculator.data.local.MedicationFullEntity

interface MedicationFullRepository {
    fun getAllMedications(): Flow<List<MedicationFullEntity>>
    fun getRegimensByMedicationId(medicationId: Long): Flow<List<DosageRegimenFullEntity>>
}