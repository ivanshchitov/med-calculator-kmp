package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.kg
import medcalculator.shared.generated.resources.patient_weight
import org.dishch.medcalculator.ui.components.InputTextField
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun WeightCard(
    weight: String,
    onWeightChanged: (String) -> Unit
) {
    AppCard(
        title = stringResource(Res.string.patient_weight),
        icon = Icons.Outlined.Scale
    ) {
        InputTextField(
            value = weight,
            onValueChange = { value: String ->
                if (value.isEmpty() || value.toDoubleOrNull() != null || value.endsWith(".")) {
                    onWeightChanged(value)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            suffix = stringResource(Res.string.kg),
            keyboardType = KeyboardType.Decimal
        )
    }
}

@Composable
@Preview
fun WeightCardPreview() {
    MedCalculatorAppTheme {
        WeightCard("12.5") {}
    }
}
