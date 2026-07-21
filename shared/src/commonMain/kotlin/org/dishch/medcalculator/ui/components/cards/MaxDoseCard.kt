package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.exceeded
import medcalculator.shared.generated.resources.maximum
import medcalculator.shared.generated.resources.not_exceeded
import medcalculator.shared.generated.resources.single_dose
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun MaxDoseCard(maxSingleDoseString: String?, isExceeded: Boolean, modifier: Modifier = Modifier) {
    val iconContainerColor = if (isExceeded) AppColors.WarningContainer else AppColors.SuccessContainer
    val iconColor = if (isExceeded) MaterialTheme.colorScheme.error else AppColors.Success
    val icon = if (isExceeded) Icons.Outlined.WarningAmber else Icons.Outlined.CheckCircle
    val textColor = if (isExceeded) MaterialTheme.colorScheme.error else AppColors.Success
    val message = if (isExceeded) stringResource(Res.string.exceeded) else stringResource(Res.string.not_exceeded)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
        colors = CardDefaults.cardColors(containerColor = AppColors.Surface),
        border = BorderStroke(AppDimens.CardBorderWidth, AppColors.Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(AppDimens.SpacingMediumSmall),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(AppDimens.ResultIconContainerSize),
                shape = RoundedCornerShape(AppDimens.CornerSmall),
                color = iconContainerColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconColor,
                        modifier = Modifier.size(AppDimens.IconSize)
                    )
                }
            }
            Spacer(modifier = Modifier.width(AppDimens.SpacingSmall))
            Column {
                Text(
                    text = stringResource(Res.string.single_dose),
                    style = MaterialTheme.typography.bodyLarge,
                    color = AppColors.TextPrimary
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
            }

            if (!maxSingleDoseString.isNullOrBlank()) {
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    horizontalAlignment = Alignment.End,
                    modifier = Modifier.width(IntrinsicSize.Min)
                ) {
                    Text(
                        text = maxSingleDoseString,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = textColor
                    )
                    Text(
                        text = stringResource(Res.string.maximum).lowercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = AppColors.TextSecondary
                    )
                }
            }
        }
    }
}
