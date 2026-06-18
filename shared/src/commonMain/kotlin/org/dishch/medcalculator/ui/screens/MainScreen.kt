package org.dishch.medcalculator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
import org.dishch.medcalculator.ui.theme.AppDimens.CardSpacing
import org.dishch.medcalculator.ui.theme.AppDimens.ScreenPadding
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {

    // Stub values
    var weight by remember { mutableStateOf("12.5") }
    var age by remember { mutableStateOf("3") }
    var ageUnit by remember { mutableStateOf(AgeUnit.YEARS) }
    val medication = remember {
        MedicationUi(
            name = "Paracetamol",
            dose = "120 mg/ml"
        )
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
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
                modifier = Modifier.padding(ScreenPadding).padding(bottom = ScreenPadding)
            ) {
                PrimaryButton(
                    text = stringResource(Res.string.calculate),
                    icon = Icons.Filled.Calculate,
                    onClick = {
                        // TODO
                    }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(CardSpacing)
        ) {

            WeightCard(
                weight = weight,
                onWeightChanged = {
                    weight = it
                }
            )

            AgeCard(
                age = age,
                unit = ageUnit,
                onAgeChanged = {
                    age = it
                },
                onUnitChanged = {
                    ageUnit = it
                }
            )

            MedicationCard(
                medicationName = medication.name,
                medicationDose = medication.dose
            )

            Spacer(
                modifier = Modifier.height(CardSpacing)
            )
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MedCalculatorAppTheme {
        MainScreen()
    }
}
