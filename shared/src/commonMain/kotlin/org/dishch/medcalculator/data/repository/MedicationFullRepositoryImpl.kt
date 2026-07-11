package org.dishch.medcalculator.data.repository

import kotlinx.coroutines.flow.Flow
import org.dishch.medcalculator.data.local.DosageRegimenFullEntity
import org.dishch.medcalculator.data.local.MedicationFullDao
import org.dishch.medcalculator.data.local.MedicationFullEntity
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.repository.MedicationFullRepository

class MedicationFullRepositoryImpl(private val dao: MedicationFullDao) : MedicationFullRepository {

    override fun getAllMedications(): Flow<List<MedicationFullEntity>> =
        dao.getAllMedications()

    override fun getRegimensByMedicationId(medicationId: String): Flow<List<DosageRegimenFullEntity>> =
        dao.getRegimensByMedicationId(medicationId)

    override fun getMedicationById(medicationId: String): Flow<MedicationFullEntity?> =
        dao.getMedicationById(medicationId)
}