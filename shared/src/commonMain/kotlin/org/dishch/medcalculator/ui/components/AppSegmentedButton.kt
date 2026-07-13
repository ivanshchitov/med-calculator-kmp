package org.dishch.medcalculator.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSegmentedButtonRow(
    modifier: Modifier = Modifier,
    space: Dp = AppDimens.CornerMediumSmall,
    content: @Composable SingleChoiceSegmentedButtonRowScope.() -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier,
        space = space,
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceSegmentedButtonRowScope.AppSegmentedButton(
    modifier: Modifier = Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    label: String
) {
    val colors = SegmentedButtonDefaults.colors(
        activeContainerColor = MaterialTheme.colorScheme.primary,
        activeContentColor = MaterialTheme.colorScheme.onPrimary,
        inactiveContainerColor = AppColors.Border,
        inactiveContentColor = AppColors.TextPrimary,
        activeBorderColor = Color.Transparent,
        inactiveBorderColor = Color.Transparent
    )
    val border = SegmentedButtonDefaults.borderStroke(Color.Transparent)
    val baseShape = RoundedCornerShape(AppDimens.CornerMediumSmall)

    SegmentedButton(
        selected = selected,
        onClick = onClick,
        shape = baseShape,
        colors = colors,
        border = border,
        icon = {},
        label = { Text(label) },
        modifier = modifier.weight(1f)
    )
}
