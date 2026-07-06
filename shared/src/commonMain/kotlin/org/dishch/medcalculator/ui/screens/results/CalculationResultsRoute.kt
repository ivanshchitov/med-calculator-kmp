package org.dishch.medcalculator.ui.screens.results

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.dishch.medcalculator.domain.model.CalculationResults

@Serializable
data class CalculationResultsRoute(
    val serializedResult: String
)

object CalculationResultSerializer {
    fun serialize(result: CalculationResults): String {
        return Json.encodeToString(result)
    }

    fun deserialize(serialized: String): CalculationResults {
        return Json.decodeFromString(serialized)
    }
}
