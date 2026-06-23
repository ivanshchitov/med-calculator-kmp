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
import androidx.compose.ui.unit.dp
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.medication_info_description
import medcalculator.shared.generated.resources.mg_per_ml_format
import org.dishch.medcalculator.domain.Medication
import org.dishch.medcalculator.domain.formattedDosage
import org.dishch.medcalculator.ui.theme.AppColors
import org.jetbrains.compose.resources.stringResource

@Composable
fun MedicationListItem(
    medication: Medication,
    onClick: () -> Unit,
    onInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val avatarColor = remember(medication.name) {
        val colors = listOf(
            Color(0xFFB39DDB), // Purple
            Color(0xFFA5D6A7), // Green
            Color(0xFFFFF59D), // Yellow
            Color(0xFF90CAF9), // Blue
            Color(0xFFEF9A9A), // Red
            Color(0xFF80CBC4), // Teal
            Color(0xFFFFCC80)  // Orange
        )
        colors[medication.name.length % colors.size]
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(avatarColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = medication.name.firstOrNull()?.uppercase() ?: "",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

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
            modifier = Modifier.padding(horizontal = 8.dp)
        )

        IconButton(
            onClick = onInfoClick,
            modifier = Modifier.size(24.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Info,
                contentDescription = stringResource(Res.string.medication_info_description, medication.name),
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}
