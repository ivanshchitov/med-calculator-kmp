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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.*
import org.dishch.medcalculator.domain.AgeUnit
import org.dishch.medcalculator.domain.CalculationResults
import org.dishch.medcalculator.domain.Medication
import org.dishch.medcalculator.domain.formattedDosage
import org.dishch.medcalculator.ui.components.ResultRow
import org.dishch.medcalculator.ui.components.cards.MaxDoseCard
import org.dishch.medcalculator.ui.components.cards.ResultCard
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun CalculationResultsScreen(
    result: CalculationResults,
    onBack: () -> Unit = {},
    viewModel: CalculationResultsViewModel = koinViewModel()
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
                    .padding(AppDimens.ScreenPadding)
                    .padding(bottom = AppDimens.ScreenPadding)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(AppDimens.ButtonCorner),
                    border = BorderStroke(2.dp, AppColors.Primary),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = AppColors.Primary)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
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
                .padding(horizontal = AppDimens.ScreenPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
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
                    value = result.weight.toString(),
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

            ResultCard {
                ResultRow(
                    icon = Icons.Outlined.Vaccines,
                    label = stringResource(Res.string.medication_dosage),
                    value = result.dosageMg.toString(),
                    unit = stringResource(Res.string.mg),
                    iconColor = AppColors.Success,
                    iconContainerColor = AppColors.SuccessContainer,
                    valueColor = AppColors.Success
                )
                ItemDivider()
                ResultRow(
                    icon = Icons.Outlined.Opacity,
                    label = stringResource(Res.string.medication_volume),
                    value = result.volumeMl.toString(),
                    unit = stringResource(Res.string.ml),
                    iconColor = AppColors.Success,
                    iconContainerColor = AppColors.SuccessContainer,
                    valueColor = AppColors.Success
                )
            }

            MaxDoseCard(isExceeded = result.isMaxDailyDoseExceeded)

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
        color = AppColors.Primary,
        modifier = Modifier.padding(top = 8.dp)
    )
}

@Composable
private fun ItemDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
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
                medication = Medication(0, "Парацетамол", 120.0, 0.0, 0),
                dosageMg = 150,
                volumeMl = 6.25,
                isMaxDailyDoseExceeded = false
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
                medication = Medication(0, "Парацетамол", 120.0, 0.0, 0),
                dosageMg = 1200,
                volumeMl = 50.0,
                isMaxDailyDoseExceeded = true
            )
        )
    }
}
