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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.launch
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.calculate
import medcalculator.shared.generated.resources.dosage_calculation
import medcalculator.shared.generated.resources.mg_per_ml_format
import org.dishch.medcalculator.domain.model.CalculationResults
import org.dishch.medcalculator.domain.model.formattedDosage
import org.dishch.medcalculator.ui.components.PrimaryButton
import org.dishch.medcalculator.ui.components.cards.AgeCard
import org.dishch.medcalculator.ui.components.cards.MedicationCard
import org.dishch.medcalculator.ui.components.cards.WeightCard
import org.dishch.medcalculator.ui.helpers.toMessage
import org.dishch.medcalculator.ui.theme.AppDimens
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    onChooseMedication: () -> Unit,
    onCalculate: (CalculationResults) -> Unit,
) {

    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isCalculationEnabled by viewModel.isCalculationEnabled.collectAsStateWithLifecycle()

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
                    .padding(AppDimens.SpacingMedium)
                    .padding(bottom = AppDimens.SpacingMedium)
            ) {
                PrimaryButton(
                    text = stringResource(Res.string.calculate),
                    icon = Icons.Filled.Calculate,
                    enabled = isCalculationEnabled,
                    onClick = {
                        focusManager.clearFocus()
                        scope.launch {
                            viewModel.calculate()?.let { result ->
                                onCalculate(result)
                            }
                        }
                    }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(AppDimens.SpacingMedium)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(AppDimens.SpacingMedium)
        ) {

            uiState.selectedMedication?.let { medication ->
                MedicationCard(
                    medicationName = medication.name,
                    medicationDose = stringResource(Res.string.mg_per_ml_format, medication.formattedDosage),
                    onClick = {
                        focusManager.clearFocus()
                        onChooseMedication()
                    }
                )
            }

            AgeCard(
                age = uiState.age,
                unit = uiState.ageUnit,
                supportingText = uiState.ageValidationError?.toMessage(),
                disableMonths = uiState.disableMonths,
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
                weight = uiState.weight,
                supportingText = uiState.weightValidationError?.toMessage(),
                onWeightChanged = viewModel::onWeightChanged,
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )
        }
    }
}
