package org.dishch.medcalculator.updates

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.dishch.medcalculator.R
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import kotlin.math.roundToInt

@Composable
fun UpdateBanner(
    state: UpdateState,
    onDownload: () -> Unit,
    onInstall: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (state is UpdateState.Idle) return

    var dismissed by rememberSaveable { mutableStateOf(false) }
    if (dismissed) return

    MedCalculatorAppTheme {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(horizontal = AppDimens.SpacingMedium)
                    // Keep the screen's bottom action button tappable.
                    .padding(bottom = AppDimens.ButtonHeight + AppDimens.SpacingMedium * 2f),
                shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
                color = AppColors.Surface,
                border = BorderStroke(1.dp, AppColors.Border),
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier.padding(AppDimens.SpacingMedium),
                    verticalArrangement = Arrangement.spacedBy(AppDimens.SpacingMediumSmall)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = stringResource(state.titleRes()),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = AppColors.TextPrimary
                            )
                            state.subtitle()?.let { subtitle ->
                                Text(
                                    text = subtitle,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = AppColors.TextSecondary
                                )
                            }
                        }
                        IconButton(onClick = { dismissed = true }) {
                            Icon(
                                imageVector = Icons.Filled.Close,
                                contentDescription = stringResource(R.string.update_action_close),
                                tint = AppColors.TextSecondary
                            )
                        }
                    }

                    when (state) {
                        is UpdateState.Downloading -> {
                            val progress = state.progress
                            if (progress != null) {
                                LinearProgressIndicator(
                                    progress = { progress },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            } else {
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                            }
                        }

                        UpdateState.Available -> Button(
                            onClick = onDownload,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.update_action_download))
                        }

                        UpdateState.ReadyToInstall -> Button(
                            onClick = onInstall,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.update_action_restart))
                        }

                        UpdateState.Installing -> LinearProgressIndicator(
                            modifier = Modifier.fillMaxWidth()
                        )

                        UpdateState.Idle -> Unit
                    }
                }
            }
        }
    }
}

private fun UpdateState.titleRes(): Int = when (this) {
    UpdateState.Available -> R.string.update_available_title
    is UpdateState.Downloading -> R.string.update_downloading_title
    UpdateState.ReadyToInstall -> R.string.update_ready_title
    UpdateState.Installing -> R.string.update_installing_title
    UpdateState.Idle -> R.string.update_available_title
}

@Composable
private fun UpdateState.subtitle(): String? = when (this) {
    UpdateState.Available -> stringResource(R.string.update_available_text)
    is UpdateState.Downloading -> progress?.let { value ->
        stringResource(R.string.update_downloading_progress, (value * 100).roundToInt())
    }

    UpdateState.ReadyToInstall -> stringResource(R.string.update_ready_text)
    UpdateState.Installing -> null
    UpdateState.Idle -> null
}
