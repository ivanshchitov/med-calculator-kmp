package org.dishch.medcalculator.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.dishch.medcalculator.data.local.DosageRegimenEntity
import org.dishch.medcalculator.data.local.MedicationDao
import org.dishch.medcalculator.data.local.toDomain
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.repository.LocaleRepository
import org.dishch.medcalculator.domain.repository.MedicationRepository

class MedicationRepositoryImpl(
    private val dao: MedicationDao,
    private val localeRepository: LocaleRepository
) : MedicationRepository {

    override fun getAllMedications(): Flow<List<Medication>> =
        dao.getAllMedications().map { entities ->
            entities.map { it.toDomain(languageCode = localeRepository.getCurrentLanguageCode()) }
        }

    override fun getRegimensByMedicationId(medicationId: String): Flow<List<DosageRegimenEntity>> =
        dao.getRegimensByMedicationId(medicationId)

    override fun getMedicationById(medicationId: String): Flow<Medication?> =
        dao.getMedicationById(medicationId).map {
            it?.toDomain(languageCode = localeRepository.getCurrentLanguageCode())
        }
}