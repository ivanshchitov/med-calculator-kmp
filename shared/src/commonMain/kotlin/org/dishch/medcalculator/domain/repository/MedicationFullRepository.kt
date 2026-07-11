package org.dishch.medcalculator.domain.repository

import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.data.local.DosageRegimenFullEntity
import org.dishch.medcalculator.data.local.MedicationFullEntity
import org.dishch.medcalculator.domain.model.Medication

interface MedicationFullRepository {
    fun getAllMedications(): Flow<List<MedicationFullEntity>>
    fun getRegimensByMedicationId(medicationId: String): Flow<List<DosageRegimenFullEntity>>
    fun getMedicationById(medicationId: String): Flow<MedicationFullEntity?>
}