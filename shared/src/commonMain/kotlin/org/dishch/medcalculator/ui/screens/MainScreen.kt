package org.dishch.medcalculator.ui.screens

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
import org.dishch.medcalculator.domain.AgeUnit
import org.dishch.medcalculator.domain.MedicationUi
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.calculate
import medcalculator.shared.generated.resources.dosage_calculation
import org.dishch.medcalculator.ui.components.cards.AgeCard
import org.dishch.medcalculator.ui.components.cards.MedicationCard
import org.dishch.medcalculator.ui.components.PrimaryButton
import org.dishch.medcalculator.ui.components.cards.WeightCard
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    selectedMedication: MedicationUi,
    onChooseMedication: () -> Unit
) {

    // Stub values
    var weight by remember { mutableStateOf("12.5") }
    var age by remember { mutableStateOf("3") }
    var ageUnit by remember { mutableStateOf(AgeUnit.YEARS) }

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
                        // TODO: Calculate and open screen with results
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
                onAgeChanged = {
                    age = it
                },
                onUnitChanged = {
                    ageUnit = it
                },
                imeAction = ImeAction.Next,
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Down)
                    }
                )
            )

            WeightCard(
                weight = weight,
                onWeightChanged = {
                    weight = it
                },
                imeAction = ImeAction.Done,
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    }
                )
            )

            MedicationCard(
                medicationName = selectedMedication.name,
                medicationDose = selectedMedication.dose,
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
        MainScreen(
            selectedMedication = MedicationUi("Парацетамол", "120 мг/мл"),
            onChooseMedication = {}
        )
    }
}
