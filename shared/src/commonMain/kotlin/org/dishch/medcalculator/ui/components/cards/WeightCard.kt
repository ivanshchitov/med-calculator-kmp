package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import medcalculator.shared.generated.resources.*
import org.dishch.medcalculator.ui.InputValidator
import org.dishch.medcalculator.ui.components.InputTextField
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.stringResource

@Composable
fun WeightCard(
    weight: String,
    onWeightChanged: (String) -> Unit,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val isError = !InputValidator.validateWeight(weight)

    AppCard(
        title = stringResource(Res.string.patient_weight),
        icon = Icons.Outlined.Scale
    ) {
        InputTextField(
            value = weight,
            onValueChange = { value: String ->
                if (InputValidator.isWeightInputValid(value)) {
                    onWeightChanged(value)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            suffix = stringResource(Res.string.kg),
            isError = isError,
            supportingText = if (isError) stringResource(Res.string.weight_supporting_text) else "",
            keyboardType = KeyboardType.Decimal,
            imeAction = imeAction,
            keyboardActions = keyboardActions
        )
    }
}

@Composable
@Preview
fun WeightCardPreview() {
    MedCalculatorAppTheme {
        WeightCard("12.5",
            onWeightChanged = {}
        )
    }
}
