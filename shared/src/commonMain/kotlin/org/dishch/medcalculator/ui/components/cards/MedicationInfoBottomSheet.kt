package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.ChildFriendly
import androidx.compose.material.icons.outlined.Face2
import androidx.compose.material.icons.outlined.Face3
import androidx.compose.material.icons.outlined.Face6
import androidx.compose.material.icons.outlined.NoAdultContent
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
import org.dishch.medcalculator.ui.theme.AppColors
import org.jetbrains.compose.resources.stringResource
import medcalculator.shared.generated.resources.*
import org.dishch.medcalculator.domain.formattedAgeRange
import org.dishch.medcalculator.domain.formattedDoseRange
import org.dishch.medcalculator.domain.toAge
import org.dishch.medcalculator.domain.formattedAgeLimit
import org.dishch.medcalculator.domain.fromAgeInYears
import org.dishch.medcalculator.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicationInfoBottomSheet(
    medication: Medication,
    regimens: List<DosageRegimen>,
    avatarColor: Color,
    sheetState: SheetState,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .padding(AppDimens.ScreenPadding)
                .navigationBarsPadding()
        ) {
            MedicationHeader(medication, avatarColor)

            Spacer(modifier = Modifier.height(AppDimens.CardSpacing))

            InfoBlockWithIcon(
                title = stringResource(Res.string.age_limit),
                value = medication.ageLimit.toAge().formattedAgeLimit,
                icon = Icons.Outlined.NoAdultContent,
                iconBackground = Color(0xFFE8EAF6),
                iconColor = AppColors.Primary
            )

            // Hidden temporarily, maybe
            // Spacer(modifier = Modifier.height(8.dp))
            //
            // InfoBlockWithIcon(
            //     title = stringResource(Res.string.max_single_dose),
            //     value = stringResource(Res.string.mg_format, medication.formattedMaxSingleDose),
            //     icon = Icons.Outlined.WarningAmber,
            //     iconBackground = Color(0xFFFFF4E5),
            //     iconColor = AppColors.Warning
            // )

            Spacer(modifier = Modifier.height(AppDimens.SpacingSmall))

            RegimensSection(regimens)

            Spacer(modifier = Modifier.height(AppDimens.ScreenPadding))
        }
    }
}

@Composable
private fun MedicationHeader(medication: Medication, avatarColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        // Avatar
        Box(
            modifier = Modifier
                .size(AppDimens.IconContainerSize)
                .background(avatarColor, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = medication.name.firstOrNull()?.uppercase() ?: "",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Color.White
            )
        }
        Spacer(modifier = Modifier.width(AppDimens.CardSpacing))
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
        shape = RoundedCornerShape(AppDimens.CardCorner),
        border = BorderStroke(AppDimens.SubCardBorderWidth, AppColors.Border),
        color = AppColors.Surface
    ) {
        Column(modifier = Modifier.padding(AppDimens.CardPadding).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.AutoMirrored.Outlined.Assignment,
                    contentDescription = null,
                    tint = AppColors.Success
                )
                Spacer(modifier = Modifier.width(AppDimens.SpacingSmall))
                Text(
                    stringResource(Res.string.dosage_regimen),
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Primary
                )
            }
            Spacer(modifier = Modifier.height(AppDimens.SpacingSmall))
            regimens.forEach { regimen ->
                RegimenItem(regimen)
                Spacer(modifier = Modifier.height(AppDimens.SpacingSmall))
            }
        }
    }
}

@Composable
private fun RegimenItem(regimen: DosageRegimen) {
    Surface(
        shape = RoundedCornerShape(AppDimens.CardCorner),
        border = BorderStroke(AppDimens.SubCardBorderWidth, AppColors.Border),
        color = AppColors.Surface
    ) {
        Row(
            modifier = Modifier.padding(AppDimens.SpacingMediumSmall).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(AppDimens.ResultIconCorner * 1.5f),
                color = Color(0xFFC8E6C9),
                modifier = Modifier.size(AppDimens.ResultIconContainerSize)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        when {
                            regimen.fromAgeInYears < 1 -> Icons.Outlined.ChildFriendly // Infant
                            regimen.fromAgeInYears < 12 -> Icons.Outlined.Face3        // Child
                            regimen.fromAgeInYears < 18 -> Icons.Outlined.Face2        // Teen
                            else -> Icons.Outlined.Face6                        // Senior
                        },
                        contentDescription = null,
                        tint = AppColors.Success
                    )
                }
            }
            Spacer(modifier = Modifier.width(AppDimens.SpacingMediumSmall))
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
        shape = RoundedCornerShape(AppDimens.CardCorner),
        border = BorderStroke(AppDimens.SubCardBorderWidth, AppColors.Border),
        color = AppColors.Surface
    ) {
        Row(
            modifier = Modifier.padding(AppDimens.CardPadding).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(AppDimens.IconContainerSize - AppDimens.CardSpacing),
                shape = RoundedCornerShape(AppDimens.ResultIconCorner * 1.5f),
                color = iconBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                }
            }
            Spacer(modifier = Modifier.width(AppDimens.CardSpacing))
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
