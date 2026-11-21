package org.dishch.medcalculator.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import medcalculator.shared.generated.resources.Res
import org.dishch.medcalculator.data.local.DosageRegimenJson
import org.dishch.medcalculator.data.local.MedicationJson
import org.dishch.medcalculator.data.local.MedicationsDataJson
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.DosageUnit
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.model.Route
import org.dishch.medcalculator.domain.repository.LocaleRepository
import org.dishch.medcalculator.domain.repository.MedicationRepository
import org.jetbrains.compose.resources.ExperimentalResourceApi

class WebMedicationRepository(
    private val localeRepository: LocaleRepository
) : MedicationRepository {

    private val json = Json { ignoreUnknownKeys = true }
    private val mutex = Mutex()
    private var cachedData: MedicationsDataJson? = null

    override fun getAllMedications(): Flow<List<Medication>> = flow {
        val language = localeRepository.getCurrentLanguageCode()
        emit(loadData().medications.map { it.toDomain(language) })
    }

    override fun getRegimensByMedicationId(medicationId: String): Flow<List<DosageRegimen>> = flow {
        val medication = loadData().medications.firstOrNull { it.id == medicationId }
        emit(medication?.dosageRegimens.toDomainRegimens(medicationId))
    }

    override fun getMedicationById(medicationId: String): Flow<Medication?> = flow {
        val language = localeRepository.getCurrentLanguageCode()
        emit(loadData().medications.firstOrNull { it.id == medicationId }?.toDomain(language))
    }

    @OptIn(ExperimentalResourceApi::class)
    private suspend fun loadData(): MedicationsDataJson {
        cachedData?.let { return it }

        val loaded = json.decodeFromString<MedicationsDataJson>(
            Res.readBytes("files/medications.json").decodeToString()
        )

        return mutex.withLock {
            cachedData ?: loaded.also { cachedData = it }
        }
    }
}

private fun MedicationJson.toDomain(language: String): Medication =
    Medication(
        id = id,
        name = if (language.lowercase() == "ru") nameRu else nameLatin,
        dosage = dosageMgPerMl,
        maxSingleDose = maxSingleDoseMg,
        ageLimit = minAgeMonths
    )

private fun List<DosageRegimenJson>?.toDomainRegimens(medicationId: String): List<DosageRegimen> =
    this.orEmpty().flatMapIndexed { regimenIndex, regimen ->
        regimen.rules.mapIndexed { ruleIndex, rule ->
            DosageRegimen(
                id = "$medicationId-${regimen.route}-$regimenIndex-$ruleIndex".hashCode().toLong(),
                fromMonths = rule.fromMonths,
                toMonths = rule.toMonths,
                fromKg = rule.fromKg,
                toKg = rule.toKg,
                minDose = rule.doseMin,
                maxDose = rule.doseMax,
                maxDoseMg = rule.maxDoseMg,
                route = Route.valueOf(regimen.route),
                dosageUnit = DosageUnit.valueOf(rule.unit),
                medicationId = medicationId
            )
        }
    }
