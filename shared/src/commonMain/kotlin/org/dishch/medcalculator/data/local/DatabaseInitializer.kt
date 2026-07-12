package org.dishch.medcalculator.data.local

import MedCalculator.shared.AppConfig
import androidx.room.useWriterConnection
import kotlinx.serialization.json.Json
import org.jetbrains.compose.resources.ExperimentalResourceApi
import medcalculator.shared.generated.resources.Res
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withContext
import org.dishch.medcalculator.data.PreferenceManager
import kotlin.Unit

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
        val dataJson = loadJsonResource("files/medications.json")
        val (medications, dosageRegimens) = DataImportConverter.convert(dataJson)
        database.useWriterConnection {
            database.replaceMedicationData(
                medications,
                dosageRegimens
            )
        }

        preferenceManager.updateCurrentStoredVersion(currentVersion)
    }
}

// Helper to reduce boilerplate code
@OptIn(ExperimentalResourceApi::class)
private suspend inline fun loadJsonResource(path: String): MedicationsDataJson =
    json.decodeFromString(Res.readBytes(path).decodeToString())
