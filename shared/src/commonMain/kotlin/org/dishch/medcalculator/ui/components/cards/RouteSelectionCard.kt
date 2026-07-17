package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.AltRoute
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.administration_routes
import org.dishch.medcalculator.domain.model.Route
import org.dishch.medcalculator.ui.components.AppSegmentedButton
import org.dishch.medcalculator.ui.components.AppSegmentedButtonRow
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSelectionCard(
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    routes: List<Route>,
    selectedRoute: Route,
    onRouteSelected: (Route) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
        border = BorderStroke(AppDimens.SubCardBorderWidth, AppColors.Border),
        color = AppColors.Surface
    ) {
        Row(
            modifier = Modifier
                .padding(AppDimens.SpacingMediumSmall)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(AppDimens.IconContainerSize - AppDimens.SpacingMedium),
                shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
                color = AppColors.InfoContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.AltRoute,
                        contentDescription = null,
                        tint = AppColors.Primary
                    )
                }
            }
            Spacer(modifier = Modifier.width(AppDimens.SpacingSmall))
            Text(
                text = stringResource(Res.string.administration_routes),
                style = textStyle,
                fontWeight = FontWeight.Bold,
                color = AppColors.Primary,
                modifier = Modifier.weight(1f)
            )

            // Segmented buttons
            AppSegmentedButtonRow(
                modifier = Modifier.wrapContentWidth(),
                space = AppDimens.CornerSmall
            ) {
                routes.forEach { route ->
                    AppSegmentedButton(
                        selected = selectedRoute == route,
                        onClick = { onRouteSelected(route) },
                        label = stringResource(route.stringRes),
                        modifier = Modifier.width(48.dp)
                    )
                }
            }
        }
    }
}
