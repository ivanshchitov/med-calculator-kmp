package org.dishch.medcalculator.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSegmentedButtonRow(
    modifier: Modifier = Modifier,
    content: @Composable SingleChoiceSegmentedButtonRowScope.() -> Unit
) {
    SingleChoiceSegmentedButtonRow(
        modifier = modifier.fillMaxWidth(),
        content = content
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleChoiceSegmentedButtonRowScope.AppSegmentedButton(
    selected: Boolean,
    onClick: () -> Unit,
    label: String,
    index: Int,
    count: Int
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
        shape = SegmentedButtonDefaults.itemShape(
            index = index,
            count = count,
            baseShape = baseShape
        ),
        colors = colors,
        border = border,
        icon = {},
        label = { Text(label) }
    )
}
