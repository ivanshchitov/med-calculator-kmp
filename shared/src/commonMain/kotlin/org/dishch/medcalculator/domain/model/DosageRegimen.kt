package org.dishch.medcalculator.domain.model

import org.dishch.medcalculator.data.local.Route
import org.dishch.medcalculator.data.local.Unit

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
