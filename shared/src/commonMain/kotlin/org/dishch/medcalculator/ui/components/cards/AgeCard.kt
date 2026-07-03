package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import medcalculator.shared.generated.resources.*
import org.dishch.medcalculator.domain.AgeUnit
import org.dishch.medcalculator.ui.InputValidator
import org.dishch.medcalculator.ui.components.AppSegmentedButton
import org.dishch.medcalculator.ui.components.AppSegmentedButtonRow
import org.dishch.medcalculator.ui.components.InputTextField
import org.dishch.medcalculator.ui.helpers.supportingText
import org.dishch.medcalculator.ui.helpers.suffix
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun AgeCard(
    age: String,
    unit: AgeUnit,
    onAgeChanged: (String) -> Unit,
    onUnitChanged: (AgeUnit) -> Unit,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val isError = !InputValidator.validateAge(age, unit)

    AppCard(
        title = stringResource(Res.string.patient_age),
        icon = Icons.Outlined.CalendarMonth
    ) {
        InputTextField(
            value = age,
            onValueChange = { value: String ->
                if (InputValidator.isAgeInputValid(value)) {
                    onAgeChanged(value)
                }
            },
            suffix = pluralStringResource(unit.suffix, age.toIntOrNull() ?: 0),
            isError = isError,
            supportingText = if (isError) stringResource(unit.supportingText) else "",
            imeAction = imeAction,
            keyboardActions = keyboardActions
        )

        Spacer(modifier = Modifier.height(AppDimens.SpacingMedium))

        AppSegmentedButtonRow {
            AppSegmentedButton(
                selected = unit == AgeUnit.MONTHS,
                onClick = { onUnitChanged(AgeUnit.MONTHS) },
                label = stringResource(Res.string.months_full)
            )
            AppSegmentedButton(
                selected = unit == AgeUnit.YEARS,
                onClick = { onUnitChanged(AgeUnit.YEARS) },
                label = stringResource(Res.string.years_full)
            )
        }
    }
}

@Composable
@Preview
fun AgeCardPreview() {
    MedCalculatorAppTheme {
        AgeCard("3", AgeUnit.YEARS,
            onUnitChanged = {},
            onAgeChanged = {}
        )
    }
}
