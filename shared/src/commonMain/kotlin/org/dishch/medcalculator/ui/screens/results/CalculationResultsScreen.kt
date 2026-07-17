package org.dishch.medcalculator.ui.screens.results

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material.icons.outlined.Opacity
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Vaccines
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.*
import org.dishch.medcalculator.domain.model.AgeUnit
import org.dishch.medcalculator.domain.model.CalculationResults
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.domain.model.Route
import org.dishch.medcalculator.domain.model.RouteCalculationResults
import org.dishch.medcalculator.domain.model.formattedDosage
import org.dishch.medcalculator.domain.model.formattedDoseRange
import org.dishch.medcalculator.domain.model.formattedVolumeRange
import org.dishch.medcalculator.formatAsDecimal
import org.dishch.medcalculator.ui.components.ResultRow
import org.dishch.medcalculator.ui.components.cards.CalculationWarningCard
import org.dishch.medcalculator.ui.components.cards.MaxDoseCard
import org.dishch.medcalculator.ui.components.cards.MedicationFullInfo
import org.dishch.medcalculator.ui.components.cards.ResultCard
import org.dishch.medcalculator.ui.components.cards.RouteSelectionCard
import org.dishch.medcalculator.ui.helpers.suffix
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun CalculationResultsScreen(
    result: CalculationResults,
    onBack: () -> Unit = {}
) {

    Scaffold(
        containerColor = AppColors.Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.calculation_results),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppColors.Background
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(AppDimens.SpacingMedium)
                    .padding(bottom = AppDimens.SpacingMedium)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AppDimens.ButtonHeight),
                    shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
                    border = BorderStroke(AppDimens.CardBorderWidth, AppColors.Primary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Primary)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(AppDimens.SpacingSmall))
                    Text(
                        text = stringResource(Res.string.new_calculation),
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(horizontal = AppDimens.SpacingMedium)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SpacingMedium)
        ) {
            SectionTitle(stringResource(Res.string.input_data))

            ResultCard {
                ResultRow(
                    icon = Icons.Outlined.CalendarMonth,
                    label = stringResource(Res.string.patient_age),
                    value = result.age.toString(),
                    unit = pluralStringResource(result.ageUnit.suffix, result.age)
                )
                ItemDivider()
                ResultRow(
                    icon = Icons.Outlined.Scale,
                    label = stringResource(Res.string.patient_weight),
                    value = result.weight.formatAsDecimal(),
                    unit = stringResource(Res.string.kg)
                )
                ItemDivider()
                ResultRow(
                    icon = Icons.Outlined.Medication,
                    label = stringResource(Res.string.medication),
                    value = result.medication.name,
                    supportingText = stringResource(Res.string.mg_per_ml_format, result.medication.formattedDosage)
                )
            }

            SectionTitle(stringResource(Res.string.calculation_results))

            val resultsByRoute = result.resultsByRoute
            var selectedRoute by rememberSaveable { mutableStateOf(resultsByRoute.keys.firstOrNull()) }
            val selectedRouteResults by remember(resultsByRoute, selectedRoute) {
                derivedStateOf {
                    selectedRoute?.let { route -> resultsByRoute[route] } ?: null
                }
            }
            val contraindicated = selectedRouteResults?.let { it.contraindicated } == true

            RouteSelectionCard(
                routes = resultsByRoute.keys.map { it },
                selectedRoute = selectedRoute ?: Route.IV,
                onRouteSelected = { route ->
                    selectedRoute = resultsByRoute.keys.find { it == route } ?: Route.IV
                }
            )

            ResultCard {
                ResultRow(
                    icon = Icons.Outlined.Vaccines,
                    label = stringResource(Res.string.medication_dosage),
                    value = if (contraindicated) {
                        stringResource(Res.string.contraindicated_short)
                    } else {
                        selectedRouteResults?.formattedDoseRange ?: ""
                    },
                    unit = if (contraindicated) "" else stringResource(Res.string.mg),
                    iconColor = AppColors.Success,
                    iconContainerColor = AppColors.SuccessContainer,
                    valueColor = if (contraindicated)  MaterialTheme.colorScheme.error else AppColors.Success
                )
                ItemDivider()
                ResultRow(
                    icon = Icons.Outlined.Opacity,
                    label = stringResource(Res.string.medication_volume),
                    value = if (contraindicated) {
                        stringResource(Res.string.contraindicated_short)
                    } else {
                        selectedRouteResults?.formattedVolumeRange ?: ""
                    },
                    unit = if (contraindicated) "" else stringResource(Res.string.ml),
                    iconColor = AppColors.Success,
                    iconContainerColor = AppColors.SuccessContainer,
                    valueColor = if (contraindicated)  MaterialTheme.colorScheme.error else AppColors.Success
                )
            }

            if (!contraindicated) {
                MaxDoseCard(isExceeded = selectedRouteResults?.isMaxDailyDoseExceeded ?: false)
            }

            SectionTitle(stringResource(Res.string.medication_info))

            MedicationFullInfo(result.medication, result.dosageRegimens, isForResults = true)

            CalculationWarningCard()
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
        color = AppColors.Primary,
        modifier = Modifier.padding(top = AppDimens.SpacingSmall)
    )
}

@Composable
private fun ItemDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = AppDimens.SpacingLargeMedium),
        thickness = 1.dp,
        color = AppColors.Border
    )
}

@Composable
@Preview
fun CalculationResultsScreenPreview() {
    MedCalculatorAppTheme {
        CalculationResultsScreen(
            result = CalculationResults(
                weight = 12.5,
                age = 3,
                ageUnit = AgeUnit.YEARS,
                medication = Medication("paracetamol", "Парацетамол", 120.0, 10.0, 0),
                dosageRegimens = listOf(),
                resultsByRoute = mapOf(
                    Route.IV to RouteCalculationResults(
                        minDoseMg = 150.0,
                        maxDoseMg = 160.0,
                        minVolMl = 6.25,
                        maxVolMl = 7.55,
                        contraindicated = true,
                        isMaxDailyDoseExceeded = false
                    ),
                    Route.IM to RouteCalculationResults(
                        minDoseMg = 150.0,
                        maxDoseMg = 160.0,
                        minVolMl = 6.25,
                        maxVolMl = 7.55,
                        contraindicated = true,
                        isMaxDailyDoseExceeded = false
                    ),
                )
            )
        )
    }
}

@Composable
@Preview
fun CalculationResultsScreenExceededPreview() {
    MedCalculatorAppTheme {
        CalculationResultsScreen(
            result = CalculationResults(
                weight = 12.5,
                age = 3,
                ageUnit = AgeUnit.YEARS,
                medication = Medication("paracetamol", "Парацетамол", 120.0, 10.0, 0),
                dosageRegimens = listOf(),
                resultsByRoute = mapOf(
                    Route.IV to RouteCalculationResults(
                        minDoseMg = 1300.0,
                        maxDoseMg = 1600.0,
                        minVolMl = 6.25,
                        maxVolMl = 7.55,
                        contraindicated = true,
                        isMaxDailyDoseExceeded = true
                    ),
                    Route.IM to RouteCalculationResults(
                        minDoseMg = 1300.0,
                        maxDoseMg = 1600.0,
                        minVolMl = 6.25,
                        maxVolMl = 7.55,
                        contraindicated = true,
                        isMaxDailyDoseExceeded = true
                    ),
                )
            )
        )
    }
}
