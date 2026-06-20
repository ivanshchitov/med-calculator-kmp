package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.exceeded
import medcalculator.shared.generated.resources.max_daily_dose
import medcalculator.shared.generated.resources.not_exceeded
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun MaxDoseCard(isExceeded: Boolean, modifier: Modifier = Modifier) {
    val backgroundColor = if (isExceeded) Color(0xFFFFEBEE) else AppColors.WarningContainer
    val iconColor = if (isExceeded) Color.Red else AppColors.Warning
    val textColor = if (isExceeded) Color.Red else AppColors.Success
    val message = if (isExceeded) stringResource(Res.string.exceeded) else stringResource(Res.string.not_exceeded)

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(AppDimens.CardCorner),
        colors = CardDefaults.cardColors(containerColor = backgroundColor),
        border = BorderStroke(2.dp, AppColors.Border),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Warning,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = stringResource(Res.string.max_daily_dose),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = AppColors.TextPrimary
                )
                Text(
                    text = message,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = textColor
                )
            }
        }
    }
}
