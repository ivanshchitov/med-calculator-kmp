package org.dishch.medcalculator.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.dishch.medcalculator.data.local.DosageRegimenDao
import org.dishch.medcalculator.data.local.DosageRegimenEntity
import org.dishch.medcalculator.data.local.MedicationDao
import org.dishch.medcalculator.data.local.MedicationEntity
import org.dishch.medcalculator.domain.DosageRegimen
import org.dishch.medcalculator.domain.Medication
import org.dishch.medcalculator.domain.MedicationRepository

class MedicationRepositoryImpl(
    private val medicationDao: MedicationDao,
    private val dosageRegimenDao: DosageRegimenDao
) : MedicationRepository {

    override fun getAllMedications(): Flow<List<Medication>> =
        medicationDao.getAllMedications().map { entities ->
            entities.map { it.toDomain() }
        }

    override suspend fun getMedicationById(id: Long): Medication? =
        medicationDao.getMedicationById(id)?.toDomain()

    override fun getRegimensForMedication(medicationId: Long): Flow<List<DosageRegimen>> =
        dosageRegimenDao.getRegimensForMedication(medicationId).map { entities ->
            entities.map { it.toDomain() }
        }

    private fun MedicationEntity.toDomain(): Medication =
        Medication(
            id = id,
            name = name,
            dosage = dosage,
            maxSingleDose = maxSingleDose,
            ageLimit = ageLimit
        )

    private fun DosageRegimenEntity.toDomain(): DosageRegimen =
        DosageRegimen(
            id = id,
            fromAge = fromAge,
            toAge = toAge,
            minDosePerKg = minDosePerKg,
            maxDosePerKg = maxDosePerKg,
            medicationId = medicationId
        )
}
