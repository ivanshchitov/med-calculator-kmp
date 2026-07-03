package org.dishch.medcalculator.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.map
import org.dishch.medcalculator.domain.model.AgeUnit

class PreferenceManager(private val dataStore: DataStore<Preferences>) {

    private companion object {
        val KEY_WEIGHT = doublePreferencesKey("weight")
        val KEY_AGE = intPreferencesKey("age")
        val KEY_AGE_UNIT = stringPreferencesKey("age_unit")
        val KEY_MEDICATION_ID = longPreferencesKey("medication_id")
        val KEY_VERSION_CODE = longPreferencesKey("version_code")

        const val DEFAULT_WEIGHT = 12.5
        const val DEFAULT_AGE = 3
        val DEFAULT_AGE_UNIT = AgeUnit.YEARS
        const val DEFAULT_MEDICATION_ID = 1L
    }

    val weight = dataStore.data.map { it[KEY_WEIGHT] ?: DEFAULT_WEIGHT }
    val age = dataStore.data.map { it[KEY_AGE] ?: DEFAULT_AGE }
    val ageUnit = dataStore.data.map {
        it[KEY_AGE_UNIT]?.let { unit ->
            runCatching { AgeUnit.valueOf(unit) }.getOrDefault(DEFAULT_AGE_UNIT)
        } ?: DEFAULT_AGE_UNIT
    }
    val medicationId = dataStore.data.map { it[KEY_MEDICATION_ID] ?: DEFAULT_MEDICATION_ID }
    val currentStoredVersion = dataStore.data.map { it[KEY_VERSION_CODE] ?: 0 }

    suspend fun save(weight: Double, age: Int, ageUnit: AgeUnit, medicationId: Long) {
        dataStore.edit { preferences ->
            preferences[KEY_WEIGHT] = weight
            preferences[KEY_AGE] = age
            preferences[KEY_AGE_UNIT] = ageUnit.name
            preferences[KEY_MEDICATION_ID] = medicationId
        }
    }

    suspend fun updateCurrentStoredVersion(versionCode: Long) {
        dataStore.edit { it[KEY_VERSION_CODE] = versionCode }
    }
}
