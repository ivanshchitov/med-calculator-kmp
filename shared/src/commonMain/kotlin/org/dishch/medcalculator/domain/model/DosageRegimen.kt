package org.dishch.medcalculator.domain.model

import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.route_im
import medcalculator.shared.generated.resources.route_iv
import medcalculator.shared.generated.resources.route_sc
import org.jetbrains.compose.resources.StringResource

enum class Route(val stringRes: StringResource) {
    IV(Res.string.route_iv),
    IM(Res.string.route_im),
    SC(Res.string.route_sc);
}
enum class DosageUnit { MG, MG_PER_KG }


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
    val dosageUnit: DosageUnit,
    val medicationId: String
)
