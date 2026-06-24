package org.dishch.medcalculator.domain

import androidx.navigation.NavType
import androidx.savedstate.SavedState
import androidx.savedstate.read
import androidx.savedstate.write
import kotlin.io.encoding.Base64
import kotlin.io.encoding.ExperimentalEncodingApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class CalculationResults(
    val weight: Double,
    val age: Int,
    val ageUnit: AgeUnit,
    val medication: Medication,
    val minDoseMg: Double,
    val maxDoseMg: Double,
    val minVolMl: Double,
    val maxVolMl: Double,
    val isMaxDailyDoseExceeded: Boolean
)

@OptIn(ExperimentalEncodingApi::class)
val CalculationResultType = object : NavType<CalculationResults>(isNullableAllowed = false) {
    override fun get(bundle: SavedState, key: String): CalculationResults? =
         bundle.read { getString(key) }.let { Json.decodeFromString(it) }

    override fun parseValue(value: String): CalculationResults =
        Json.decodeFromString(Base64.UrlSafe.decode(value).decodeToString())

    override fun put(bundle: SavedState, key: String, value: CalculationResults) {
        bundle.write {
            putString(key, Json.encodeToString(value))
        }
    }

    override fun serializeAsValue(value: CalculationResults): String =
        Base64.UrlSafe.encode(Json.encodeToString(value).encodeToByteArray())
}
