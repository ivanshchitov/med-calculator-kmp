package org.dishch.medcalculator.data.local

import kotlinx.serialization.Serializable

@Serializable
data class MedicationJson(
    val id: String,
    val nameRu: String,
    val nameLatin: String,
    val dosageMgPerMl: Double,
    val minAgeMonths: Int,
    val maxSingleDoseMg: Double?,
    val dosageRegimens: List<DosageRegimenJson>
)

@Serializable
data class DosageRegimenJson(
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
data class MedicationsDataJson(
    val medications: List<MedicationJson>
)
