package org.dishch.medcalculator.ui.components.cards

import androidx.collection.forEach
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.NoAdultContent
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.model.formattedDosage
import org.dishch.medcalculator.ui.helpers.formattedAgeLimit
import org.dishch.medcalculator.ui.helpers.formattedAgeRange
import org.dishch.medcalculator.ui.theme.AppColors
import org.jetbrains.compose.resources.stringResource
import medcalculator.shared.generated.resources.*
import org.dishch.medcalculator.domain.model.formattedMaxSingleDose
import org.dishch.medcalculator.domain.model.toAge
import org.dishch.medcalculator.ui.helpers.doseDisplayString
import org.dishch.medcalculator.ui.helpers.icon
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.AppDimens.SpacingSmall

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
                .padding(AppDimens.SpacingMedium)
                .navigationBarsPadding()
                .verticalScroll(rememberScrollState())
        ) {
            MedicationHeader(medication, avatarColor)

            Spacer(modifier = Modifier.height(AppDimens.SpacingMedium))

            InfoBlockWithIcon(
                title = stringResource(Res.string.age_limit),
                value = medication.ageLimit.toAge().formattedAgeLimit,
                icon = Icons.Outlined.NoAdultContent,
                iconBackground = AppColors.InfoContainer,
                iconColor = AppColors.Primary
            )

             Spacer(modifier = Modifier.height(SpacingSmall))

             if (medication.maxSingleDose != null) {
                 InfoBlockWithIcon(
                     title = stringResource(Res.string.max_single_dose),
                     value = stringResource(Res.string.mg_format, medication.formattedMaxSingleDose),
                     icon = Icons.Outlined.WarningAmber,
                     iconBackground = AppColors.WarningContainer,
                     iconColor = AppColors.Warning
                 )

                 Spacer(modifier = Modifier.height(AppDimens.SpacingSmall))
             }

            val uniqueRoutes = regimens.map { it.route }.filterNotNull().distinct()
            var selectedRoute by rememberSaveable { mutableStateOf(uniqueRoutes.firstOrNull()) }

            if (uniqueRoutes.isNotEmpty() && selectedRoute != null) {
                RouteSelectionCard(
                    routes = uniqueRoutes.map { it },
                    selectedRoute = selectedRoute!!,
                    onRouteSelected = { route ->
                        selectedRoute = uniqueRoutes.find { it == route }
                    }
                )
                Spacer(modifier = Modifier.height(AppDimens.SpacingSmall))
            }

            val filteredRegimens by remember(regimens, selectedRoute) {
                derivedStateOf {
                    selectedRoute?.let { route -> regimens.filter { it.route == route } } ?: regimens
                }
            }

            RegimensSection(filteredRegimens)

            Spacer(modifier = Modifier.height(AppDimens.SpacingMedium))
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
        Spacer(modifier = Modifier.width(AppDimens.SpacingMedium))
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
        shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
        border = BorderStroke(AppDimens.SubCardBorderWidth, AppColors.Border),
        color = AppColors.Surface
    ) {
        Column(modifier = Modifier.padding(AppDimens.SpacingLargeMedium).fillMaxWidth()) {
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
        shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
        border = BorderStroke(AppDimens.SubCardBorderWidth, AppColors.Border),
        color = AppColors.Surface
    ) {
        Row(
            modifier = Modifier.padding(AppDimens.SpacingMediumSmall).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
                color = AppColors.SuccessContainer,
                modifier = Modifier.size(AppDimens.ResultIconContainerSize)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = regimen.icon,
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
                    text = regimen.doseDisplayString,
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
        shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
        border = BorderStroke(AppDimens.SubCardBorderWidth, AppColors.Border),
        color = AppColors.Surface
    ) {
        Row(
            modifier = Modifier.padding(AppDimens.SpacingMediumSmall).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(AppDimens.IconContainerSize - AppDimens.SpacingMedium),
                shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
                color = iconBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                }
            }
            Spacer(modifier = Modifier.width(AppDimens.SpacingMedium))
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
