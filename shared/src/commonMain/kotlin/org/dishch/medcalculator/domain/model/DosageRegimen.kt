package org.dishch.medcalculator.domain.model

enum class Route { IV, IM, SC }
enum class Unit { MG, MG_PER_KG }


data class DosageRegimen(
    val id: Long,
    val fromMonths: Int,
    val toMonths: Int,
    val fromKg: Double?,
    val toKg: Double?,
    val minDose: Double?,
    val maxDose: Double?,
    val maxDoseMg: Double?,
    val route: Route,
    val unit: Unit,
    val medicationId: String
)
