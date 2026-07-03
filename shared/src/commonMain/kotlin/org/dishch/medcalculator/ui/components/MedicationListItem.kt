package org.dishch.medcalculator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.medication_info_description
import medcalculator.shared.generated.resources.mg_per_ml_format
import org.dishch.medcalculator.domain.Medication
import org.dishch.medcalculator.domain.formattedDosage
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppColors.AvatarColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.jetbrains.compose.resources.stringResource

@Composable
fun MedicationListItem(
    medication: Medication,
    onClick: () -> Unit,
    onInfoClick: (Color) -> Unit,
    modifier: Modifier = Modifier
) {
    val avatarColor = remember(medication.name) {
        AvatarColors[(medication.name.hashCode() and Int.MAX_VALUE) % AvatarColors.size]
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(
                start = AppDimens.SpacingMedium,
                end = AppDimens.SpacingExtraSmall,
                top = AppDimens.SpacingMediumSmall,
                bottom = AppDimens.SpacingMediumSmall
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(AppDimens.ResultIconContainerSize)
                .background(avatarColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = medication.name.firstOrNull()?.uppercase() ?: "",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(AppDimens.SpacingMedium))

        Text(
            text = medication.name,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = AppColors.TextPrimary,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = stringResource(Res.string.mg_per_ml_format, medication.formattedDosage),
            style = MaterialTheme.typography.bodyMedium,
            color = AppColors.TextSecondary,
            modifier = Modifier.padding(horizontal = AppDimens.SpacingSmall)
        )

        IconButton(
            onClick = {
                onInfoClick(avatarColor)
            },
            modifier = Modifier.size(AppDimens.ButtonHeight)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(Res.string.medication_info_description, medication.name),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
