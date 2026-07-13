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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.administration_routes
import org.dishch.medcalculator.domain.model.Route
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RouteSelectionCard(
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
            Spacer(modifier = Modifier.width(AppDimens.SpacingMedium))
            Text(
                text = stringResource(Res.string.administration_routes),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = AppColors.Primary,
                modifier = Modifier.weight(1f)
            )

            // Segmented buttons
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.wrapContentWidth()
            ) {
                routes.forEach { route ->
                    val isSelected = selectedRoute == route

                    SegmentedButton(
                        selected = isSelected,
                        onClick = { onRouteSelected(route) },
                        shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = route.colors.container,
                            activeContentColor = route.colors.content,
                            inactiveContainerColor = Color.Transparent,
                            inactiveContentColor = AppColors.TextSecondary
                        ),
                        border = BorderStroke(0.dp, Color.Transparent),
                        label = { Text(stringResource(route.stringRes)) },
                        icon = {},
                        modifier = Modifier.width(48.dp)
                    )
                }
            }
        }
    }
}
