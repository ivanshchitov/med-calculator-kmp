package org.dishch.medcalculator.domain.model

import androidx.compose.ui.graphics.Color
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.route_im
import medcalculator.shared.generated.resources.route_iv
import medcalculator.shared.generated.resources.route_sc
import org.dishch.medcalculator.ui.theme.AppColors.RouteIM
import org.dishch.medcalculator.ui.theme.AppColors.RouteIMContainer
import org.dishch.medcalculator.ui.theme.AppColors.RouteIV
import org.dishch.medcalculator.ui.theme.AppColors.RouteIVContainer
import org.dishch.medcalculator.ui.theme.AppColors.RouteSC
import org.dishch.medcalculator.ui.theme.AppColors.RouteSCContainer
import org.jetbrains.compose.resources.StringResource

enum class Route(
    val stringRes: StringResource,
    val colors: RouteColors
) {
    IV(
        Res.string.route_iv,
        RouteColors(RouteIV, RouteIVContainer)
    ),
    IM(
        Res.string.route_im,
        RouteColors(RouteIM, RouteIMContainer)
    ),
    SC(
        Res.string.route_sc,
        RouteColors(RouteSC, RouteSCContainer)
    );
}
enum class DosageUnit { MG, MG_PER_KG }

data class RouteColors(
    val content: Color,
    val container: Color
)

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
