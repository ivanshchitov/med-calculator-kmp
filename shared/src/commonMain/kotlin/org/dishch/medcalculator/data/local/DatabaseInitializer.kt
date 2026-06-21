package org.dishch.medcalculator.data.local

import androidx.room.Transaction
import androidx.room.useWriterConnection
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.ExperimentalResourceApi
import medcalculator.shared.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext

@Serializable
data class MedicationJson(
    val id: Long,
    val name: String,
    val dosageMgPerMl: Int,
    val maxSingleDoseMg: Int,
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

suspend fun initializeDatabase(database: AppDatabase): Result<Unit> = withContext(Dispatchers.IO) {
    runCatching {
        val medications = loadJsonResource<List<MedicationJson>>("files/medications.json")
        val dosageRegimens = loadJsonResource<List<DosageRegimenJson>>("files/dosage_regimens.json")

        database.useWriterConnection {
            clearAndInsertNewData(
                database.getMedicationDao(),
                database.getDosageRegimenDao(),
                medications,
                dosageRegimens
            )
        }
    }.onFailure { e ->
        println("Failed to initialize database: ${e.message}")
    }
}

@Transaction
private suspend fun clearAndInsertNewData(
    medDao: MedicationDao,
    regimenDao: DosageRegimenDao,
    medications: List<MedicationJson>,
    dosageRegimens: List<DosageRegimenJson>) {
    // Clear database
    regimenDao.deleteAll()
    medDao.deleteAll()
    // Insert data
    medDao.insertAll(medications.map { it.toEntity() })
    regimenDao.insertAll(dosageRegimens.map { it.toEntity() })
}

// Helper to reduce boilerplate code
@OptIn(ExperimentalResourceApi::class)
private suspend inline fun <reified T> loadJsonResource(path: String): T =
    json.decodeFromString(Res.readBytes(path).decodeToString())
