package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.ChildFriendly
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.dishch.medcalculator.domain.DosageRegimen
import org.dishch.medcalculator.domain.Medication
import org.dishch.medcalculator.domain.formattedDosage
import org.dishch.medcalculator.domain.formattedMaxSingleDose
import org.dishch.medcalculator.ui.theme.AppColors
import org.jetbrains.compose.resources.stringResource
import medcalculator.shared.generated.resources.*
import org.dishch.medcalculator.domain.formattedAgeRange
import org.dishch.medcalculator.domain.formattedDoseRange
import org.dishch.medcalculator.domain.toAge
import org.dishch.medcalculator.domain.formattedAgeLimit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationInfoBottomSheet(
    medication: Medication,
    regimens: List<DosageRegimen>,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .navigationBarsPadding()
        ) {
            MedicationHeader(medication)

            Spacer(modifier = Modifier.height(16.dp))

            InfoBlockWithIcon(
                title = stringResource(Res.string.age_limit),
                value = medication.ageLimit.toAge().formattedAgeLimit,
                icon = Icons.Outlined.ChildFriendly,
                iconBackground = Color(0xFFE8EAF6),
                iconColor = AppColors.Primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            InfoBlockWithIcon(
                title = stringResource(Res.string.max_single_dose),
                value = stringResource(Res.string.mg_format, medication.formattedMaxSingleDose),
                icon = Icons.Outlined.WarningAmber,
                iconBackground = Color(0xFFFFF4E5),
                iconColor = AppColors.Warning
            )

            Spacer(modifier = Modifier.height(8.dp))

            RegimensSection(regimens)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun MedicationHeader(medication: Medication) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color(0xFFE8EAF6), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Outlined.Medication, contentDescription = null, tint = AppColors.Primary)
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                medication.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                stringResource(Res.string.dosage_label, medication.formattedDosage),
                style = MaterialTheme.typography.bodyMedium,
                color = AppColors.TextSecondary
            )
        }
    }
}

@Composable
private fun RegimensSection(regimens: List<DosageRegimen>) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, AppColors.Border),
        color = AppColors.Surface
    ) {
        Column(modifier = Modifier.padding(16.dp).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.AutoMirrored.Outlined.Assignment,
                    contentDescription = null,
                    tint = AppColors.Success
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(Res.string.dosage_regimen),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            regimens.forEach { regimen ->
                RegimenItem(regimen)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Composable
private fun RegimenItem(regimen: DosageRegimen) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, AppColors.Border),
        color = AppColors.Surface
    ) {
        Row(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFFC8E6C9),
                modifier = Modifier.size(40.dp)
            ) {}
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    regimen.formattedAgeRange,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextPrimary
                )
                Text(
                    stringResource(Res.string.dosage_per_kg_format, regimen.formattedDoseRange),
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
            }
        }
    }
}

@Composable
private fun InfoBlockWithIcon(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    iconBackground: Color
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, AppColors.Border),
        color = AppColors.Surface
    ) {
        Row(
            modifier = Modifier.padding(16.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(12.dp),
                color = iconBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextPrimary
                )
                Text(
                    value,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.TextPrimary
                )
            }
        }
    }
}
