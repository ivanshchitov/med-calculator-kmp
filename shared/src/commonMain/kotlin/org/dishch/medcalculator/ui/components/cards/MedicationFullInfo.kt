package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Assignment
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.NoAdultContent
import androidx.compose.material.icons.outlined.WarningAmber
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.age_limit
import medcalculator.shared.generated.resources.dosage_regimen
import medcalculator.shared.generated.resources.max_single_dose
import medcalculator.shared.generated.resources.maximum
import medcalculator.shared.generated.resources.medication
import medcalculator.shared.generated.resources.mg_format
import medcalculator.shared.generated.resources.mg_per_ml_format
import org.dishch.medcalculator.domain.model.DosageRegimen
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.model.formattedDosage
import org.dishch.medcalculator.domain.model.formattedMaxSingleDose
import org.dishch.medcalculator.domain.model.isWeightRangeValid
import org.dishch.medcalculator.ui.helpers.doseDisplayString
import org.dishch.medcalculator.ui.helpers.formattedAgeRange
import org.dishch.medcalculator.ui.helpers.formattedMaxSingleDose
import org.dishch.medcalculator.ui.helpers.formattedWeightRange
import org.dishch.medcalculator.ui.helpers.icon
import org.dishch.medcalculator.ui.helpers.toFormattedAgeLimit
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.AppDimens.SpacingExtraSmall
import org.dishch.medcalculator.ui.theme.AppDimens.SpacingSmall
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun MedicationFullInfo(
    medication: Medication,
    regimens: List<DosageRegimen>,
    isForResults: Boolean = false,
    modifier: Modifier = Modifier.fillMaxWidth()
) {
    Column(
        modifier = modifier
    ) {
        if (isForResults) {
            InfoBlockWithIcon(
                title = stringResource(Res.string.medication),
                value = "${medication.name}, ${stringResource(Res.string.mg_per_ml_format, medication.formattedDosage)}",
                icon = Icons.Outlined.Medication,
                iconBackground = AppColors.InfoContainer,
                iconColor = AppColors.Primary
            )
            Spacer(modifier = Modifier.height(SpacingSmall))
        }

        InfoBlockWithIcon(
            title = stringResource(Res.string.age_limit),
            value = medication.ageLimit.toFormattedAgeLimit(),
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
            Spacer(modifier = Modifier.height(SpacingSmall))
        }

        val uniqueRoutes = regimens.map { it.route }.distinct()
        var selectedRoute by rememberSaveable { mutableStateOf(uniqueRoutes.firstOrNull()) }

        if (uniqueRoutes.isNotEmpty() && selectedRoute != null) {
            RouteSelectionCard(
                routes = uniqueRoutes.map { it },
                selectedRoute = selectedRoute!!,
                isSubCard = true,
                onRouteSelected = { route ->
                    selectedRoute = uniqueRoutes.find { it == route }
                }
            )
            Spacer(modifier = Modifier.height(SpacingSmall))
        }

        val filteredRegimens by remember(regimens, selectedRoute) {
            derivedStateOf {
                selectedRoute?.let { route -> regimens.filter { it.route == route } } ?: regimens
            }
        }

        RegimensSection(filteredRegimens)
    }
}

@Composable
private fun RegimensSection(regimens: List<DosageRegimen>) {
    Surface(
        shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
        border = BorderStroke(AppDimens.SubCardBorderWidth, AppColors.Border),
        color = AppColors.Surface
    ) {
        Column(modifier = Modifier.padding(
            horizontal = AppDimens.SpacingMediumSmall
        ).fillMaxWidth()) {
            Row(
                modifier = Modifier.padding(top = AppDimens.SpacingMediumSmall),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.AutoMirrored.Outlined.Assignment,
                    contentDescription = null,
                    tint = AppColors.Success
                )
                Spacer(modifier = Modifier.width(SpacingSmall))
                Text(
                    stringResource(Res.string.dosage_regimen),
                    fontWeight = FontWeight.Bold,
                    color = AppColors.Success
                )
            }
            Spacer(modifier = Modifier.height(SpacingExtraSmall))
            regimens.forEachIndexed { index, regimen ->
                RegimenItem(regimen)
                if (index != regimens.lastIndex)
                    ItemDivider()
            }
        }
    }
}

@Composable
private fun ItemDivider() {
    HorizontalDivider(
        thickness = 1.dp,
        color = AppColors.Border
    )
}

@Composable
private fun RegimenItem(regimen: DosageRegimen) {
    Row(
        modifier = Modifier.padding(
            vertical = AppDimens.SpacingMediumSmall,
            horizontal = AppDimens.SpacingExtraSmall
        ).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = RoundedCornerShape(AppDimens.CornerSmall),
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
        Spacer(modifier = Modifier.width(SpacingSmall))
        Column {
            Row(modifier = Modifier.height(IntrinsicSize.Min)) {
                Text(
                    regimen.formattedAgeRange,
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextPrimary
                )
                if (regimen.isWeightRangeValid()) {
                    VerticalDivider(
                        modifier = Modifier.fillMaxHeight().padding(AppDimens.SpacingExtraSmall),
                        thickness = AppDimens.SubCardBorderWidth,
                        color = Color.Gray
                    )
                    Text(
                        regimen.formattedWeightRange,
                        style = MaterialTheme.typography.bodyMedium,
                        color = AppColors.TextPrimary
                    )
                }
            }
            Text(
                text = regimen.doseDisplayString,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = AppColors.TextPrimary
            )
        }

        if (regimen.maxDoseMg != null) {
            Spacer(modifier = Modifier.weight(1f))
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.width(IntrinsicSize.Min)
            ) {
                Text(
                    text = stringResource(Res.string.maximum),
                    style = MaterialTheme.typography.bodyMedium,
                    color = AppColors.TextPrimary
                )
                Text(
                    text = regimen.formattedMaxSingleDose,
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
                modifier = Modifier.size(AppDimens.ResultIconContainerSize),
                shape = RoundedCornerShape(AppDimens.CornerSmall),
                color = iconBackground
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = iconColor)
                }
            }
            Spacer(modifier = Modifier.width(SpacingSmall))
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

@Composable
@Preview
fun InfoBlockWithIconPreview() {
    MedCalculatorAppTheme {
        InfoBlockWithIcon(
            title = "Препарат",
            value = "Спазматен, 500 мг/мл",
            icon = Icons.Outlined.Medication,
            iconColor = AppColors.Primary,
            iconBackground = AppColors.InfoContainer
        )
    }
}