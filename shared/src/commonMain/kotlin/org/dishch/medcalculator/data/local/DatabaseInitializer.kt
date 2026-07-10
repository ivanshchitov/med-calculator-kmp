package org.dishch.medcalculator.data.local

import MedCalculator.shared.AppConfig
import androidx.room.useWriterConnection
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import medcalculator.shared.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.dishch.medcalculator.data.PreferenceManager
import org.dishch.medcalculator.getPlatform
import org.koin.core.logger.Logger

@Serializable
data class MedicationJson(
    val id: Long,
    val name: String,
    val dosageMgPerMl: Double,
    val maxSingleDoseMg: Double,
    val minAgeMonths: Int = 0
) {
    fun toEntity() = MedicationEntity(
        id = id,
        name = name,
        dosage = dosageMgPerMl,
        maxSingleDose = maxSingleDoseMg,
        ageLimit = minAgeMonths
    )
}

@Serializable
data class DosageRegimenJson(
    val id: Long,
    val fromAge: Int,
    val toAge: Int,
    val minDosePerKg: Double,
    val maxDosePerKg: Double,
    val medId: Long
) {
    fun toEntity() = DosageRegimenEntity(
        fromAge = fromAge,
        toAge = toAge,
        minDosePerKg = minDosePerKg,
        maxDosePerKg = maxDosePerKg,
        medicationId = medId
    )
}

private val json = Json { ignoreUnknownKeys = true }

suspend fun initializeDatabase(
    database: AppDatabase,
    preferenceManager: PreferenceManager
): Result<Unit> = runCatching {
    val currentVersion = AppConfig.VERSION_CODE
    val storedVersion = preferenceManager.currentStoredVersion.first()

    if (storedVersion >= currentVersion)
        return@runCatching

    withContext(Dispatchers.IO) {
        val medications = loadJsonResource<List<MedicationJson>>("files/medications.json")
        val dosageRegimens = loadJsonResource<List<DosageRegimenJson>>("files/dosage_regimens.json")
        database.useWriterConnection {
            database.replaceMedicationData(
                medications.map { it.toEntity() },
                dosageRegimens.map { it.toEntity() }
            )
        }
        
        // Load full data
        val fullDataJson = loadJsonResource<FullDataJson>("files/medications_full.json")
        val (fullMedications, fullRegimens) = DataImportConverter.convert(fullDataJson)
        database.useWriterConnection {
            database.replaceMedicationFullData(
                fullMedications,
                fullRegimens
            )
        }

        preferenceManager.updateCurrentStoredVersion(currentVersion)
    }
}

// Helper to reduce boilerplate code
@OptIn(ExperimentalResourceApi::class)
private suspend inline fun <reified T> loadJsonResource(path: String): T =
    json.decodeFromString(Res.readBytes(path).decodeToString())
