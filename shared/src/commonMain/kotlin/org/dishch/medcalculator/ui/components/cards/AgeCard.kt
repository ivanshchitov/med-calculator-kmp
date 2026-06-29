package org.dishch.medcalculator.ui.components.cards

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import org.dishch.medcalculator.domain.AgeUnit
import medcalculator.shared.generated.resources.*
import org.dishch.medcalculator.ui.components.InputTextField
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgeCard(
    age: String,
    unit: AgeUnit,
    onAgeChanged: (String) -> Unit,
    onUnitChanged: (AgeUnit) -> Unit,
    imeAction: ImeAction = ImeAction.Default,
    keyboardActions: KeyboardActions = KeyboardActions.Default
) {
    val ageInt = age.toIntOrNull()
    val isError = when (unit) {
        AgeUnit.MONTHS -> ageInt == null || ageInt !in 1..11
        AgeUnit.YEARS -> ageInt == null || ageInt !in 1..17
    }

    AppCard(
        title = stringResource(Res.string.patient_age),
        icon = Icons.Outlined.CalendarMonth
    ) {
        InputTextField(
            value = age,
            onValueChange = { value: String ->
                if (value.isEmpty() || (value.all { it.isDigit() } && value.length <= 2)) {
                    onAgeChanged(value)
                }
            },
            suffix = pluralStringResource(unit.suffix, ageInt ?: 0),
            isError = isError,
            supportingText = if (isError) stringResource(unit.supportingText) else "",
            imeAction = imeAction,
            keyboardActions = keyboardActions
        )

        Spacer(modifier = Modifier.height(16.dp))

        SingleChoiceSegmentedButtonRow(
            modifier = Modifier.fillMaxWidth()
        ) {
            val colors = SegmentedButtonDefaults.colors(
                activeContainerColor = MaterialTheme.colorScheme.primary,
                activeContentColor = MaterialTheme.colorScheme.onPrimary,
                inactiveContainerColor = Color(0xFFF2F4F8),
                inactiveContentColor = MaterialTheme.colorScheme.onSurface,
                activeBorderColor = Color.Transparent,
                inactiveBorderColor = Color.Transparent
            )
            val border = SegmentedButtonDefaults.borderStroke(Color.Transparent)
            val baseShape = RoundedCornerShape(AppDimens.ButtonCorner)

            SegmentedButton(
                selected = unit == AgeUnit.MONTHS,
                onClick = { onUnitChanged(AgeUnit.MONTHS) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 0,
                    count = 2,
                    baseShape = baseShape
                ),
                colors = colors,
                border = border,
                icon = {}
            ) {
                Text(stringResource(Res.string.months_full))
            }

            SegmentedButton(
                selected = unit == AgeUnit.YEARS,
                onClick = { onUnitChanged(AgeUnit.YEARS) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = 1,
                    count = 2,
                    baseShape = baseShape
                ),
                colors = colors,
                border = border,
                icon = {}
            ) {
                Text(stringResource(Res.string.years_full))
            }
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
