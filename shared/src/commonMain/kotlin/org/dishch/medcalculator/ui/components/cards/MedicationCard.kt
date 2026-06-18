package org.dishch.medcalculator.ui.components.cards

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.Medication
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.chosen_medication
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun MedicationCard(
    medicationName: String,
    medicationDose: String
) {
    AppCard(
        title = stringResource(Res.string.chosen_medication),
        icon = Icons.Outlined.Medication,
        verticalAlignment = Alignment.CenterVertically,
        trailingIcon = Icons.AutoMirrored.Filled.KeyboardArrowRight
    ) {
        Text(
            text = medicationName,
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text = medicationDose,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
@Preview
fun MedicationCardPreview() {
    MedCalculatorAppTheme {
        MedicationCard("Paracetamol", "120 mg/ml")
    }
}
