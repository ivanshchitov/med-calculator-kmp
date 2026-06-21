package org.dishch.medcalculator.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.dishch.medcalculator.data.local.MedicationDao
import org.dishch.medcalculator.data.local.MedicationEntity
import org.dishch.medcalculator.domain.Medication
import org.dishch.medcalculator.domain.MedicationRepository

class MedicationRepositoryImpl(
    private val medicationDao: MedicationDao
) : MedicationRepository {

    override fun getAllMedications(): Flow<List<Medication>> {
        return medicationDao.getAllMedications().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    private fun MedicationEntity.toDomain(): Medication {
        return Medication(
            id = id,
            name = name,
            dosage = dosage,
            maxSingleDose = maxSingleDose,
            ageLimit = ageLimit
        )
    }
}
