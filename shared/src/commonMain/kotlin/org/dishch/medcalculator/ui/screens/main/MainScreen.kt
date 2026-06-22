package org.dishch.medcalculator.ui.screens.main

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.dishch.medcalculator.domain.Medication
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.calculate
import medcalculator.shared.generated.resources.dosage_calculation
import medcalculator.shared.generated.resources.mg_per_ml_format
import org.dishch.medcalculator.domain.CalculationResults
import org.dishch.medcalculator.domain.formattedDosage
import org.dishch.medcalculator.ui.components.cards.AgeCard
import org.dishch.medcalculator.ui.components.cards.MedicationCard
import org.dishch.medcalculator.ui.components.PrimaryButton
import org.dishch.medcalculator.ui.components.cards.WeightCard
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun MainScreen(
    selectedMedication: Medication,
    onChooseMedication: () -> Unit,
    onCalculate: (CalculationResults) -> Unit,
    viewModel: MainViewModel = koinViewModel()
) {

    val weight by viewModel.weight.collectAsStateWithLifecycle()
    val age by viewModel.age.collectAsStateWithLifecycle()
    val ageUnit by viewModel.ageUnit.collectAsStateWithLifecycle()

    val focusManager = LocalFocusManager.current

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.dosage_calculation),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .padding(AppDimens.ScreenPadding)
                    .padding(bottom = AppDimens.ScreenPadding)
            ) {
                PrimaryButton(
                    text = stringResource(Res.string.calculate),
                    icon = Icons.Filled.Calculate,
                    onClick = {
                        focusManager.clearFocus()
                        val result = CalculationResults(
                            weight = weight.toDoubleOrNull() ?: 0.0,
                            age = age.toIntOrNull() ?: 0,
                            ageUnit = ageUnit,
                            medication = selectedMedication,
                            dosageMg = 150, // Stub
                            volumeMl = 6.25, // Stub
                            isMaxDailyDoseExceeded = false // Stub
                        )
                        onCalculate(result)
                    }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppDimens.CardSpacing)
        ) {

            AgeCard(
                age = age,
                unit = ageUnit,
                onAgeChanged = viewModel::onAgeChanged,
                onUnitChanged = viewModel::onAgeUnitChanged,
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            WeightCard(
                weight = weight,
                onWeightChanged = viewModel::onWeightChanged,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            MedicationCard(
                medicationName = selectedMedication.name,
                medicationDose = stringResource(Res.string.mg_per_ml_format, selectedMedication.formattedDosage),
                onClick = {
                    focusManager.clearFocus()
                    onChooseMedication()
                }
            )

            Spacer(
                modifier = Modifier.height(AppDimens.CardSpacing)
            )
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MedCalculatorAppTheme {
        // NOTE: Preview might need to be adjusted or use a MockViewModel
        MainScreen(
            selectedMedication = Medication(0, "Парацетамол", 120.0, 0.0, 0),
            onChooseMedication = {},
            onCalculate = {}
        )
    }
}
