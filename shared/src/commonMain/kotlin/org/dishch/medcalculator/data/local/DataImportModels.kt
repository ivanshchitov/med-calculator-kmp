package org.dishch.medcalculator.data.local

import kotlinx.serialization.Serializable

@Serializable
data class MedicationFullJson(
    val id: String,
    val nameRu: String,
    val nameLatin: String,
    val dosageMgPerMl: Double,
    val minAgeMonths: Int,
    val maxSingleDoseMg: Double?,
    val dosageRegimens: List<RegimenJson>
)

@Serializable
data class RegimenJson(
    val route: String,
    val rules: List<RuleJson>
)

@Serializable
data class RuleJson(
    val fromMonths: Int,
    val toMonths: Int,
    val fromKg: Double?,
    val toKg: Double?,
    val doseMin: Double?,
    val doseMax: Double?,
    val unit: String,
    val maxDoseMg: Double?,
    val note: String?
)

@Serializable
data class FullDataJson(
    val medications: List<MedicationFullJson>
)
