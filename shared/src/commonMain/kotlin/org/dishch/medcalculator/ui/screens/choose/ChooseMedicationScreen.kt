package org.dishch.medcalculator.ui.screens.choose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.coroutines.yield
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.choose_medication
import medcalculator.shared.generated.resources.search
import org.dishch.medcalculator.domain.model.Medication
import org.dishch.medcalculator.ui.components.MedicationListItem
import org.dishch.medcalculator.ui.components.cards.MedicationInfoBottomSheet
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.annotation.KoinExperimentalAPI

@OptIn(ExperimentalMaterial3Api::class, KoinExperimentalAPI::class)
@Composable
fun ChooseMedicationScreen(
    onMedicationSelected: (Medication) -> Unit,
    onBack: () -> Unit,
    viewModel: ChooseMedicationViewModel = koinViewModel()
) {
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val filteredMedications by viewModel.filteredMedications.collectAsStateWithLifecycle()
    val showInfo by viewModel.showInfo.collectAsStateWithLifecycle()
    val selectedMedication by viewModel.selectedMedication.collectAsStateWithLifecycle()
    val selectedMedicationColor by viewModel.selectedMedicationColor.collectAsStateWithLifecycle()
    val regimens by viewModel.regimens.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState()

    val focusManager = LocalFocusManager.current

    // Synchronize internal animation finish to state disposal
    LaunchedEffect(sheetState.targetValue) {
        if (sheetState.targetValue == SheetValue.Hidden) {
            yield()
            viewModel.onDismissInfo()
        }
    }

    Scaffold(
        modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(onTap = {
                focusManager.clearFocus()
            })
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(Res.string.choose_medication),
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->

        if (showInfo) {
            MedicationInfoBottomSheet(
                medication = selectedMedication!!,
                avatarColor = selectedMedicationColor,
                regimens = regimens,
                sheetState = sheetState,
                onDismiss = {
                    focusManager.clearFocus()
                    viewModel.onDismissInfo()
                }
            )
        }

        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = AppDimens.SpacingMedium)
                .padding(bottom = AppDimens.SpacingMedium)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = viewModel::onSearchQueryChange,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = AppDimens.SpacingMedium),
                placeholder = { Text(stringResource(Res.string.search)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                singleLine = true,
                shape = RoundedCornerShape(AppDimens.CornerMediumSmall),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedContainerColor = AppColors.Surface,
                    unfocusedContainerColor = AppColors.Surface,
                    focusedBorderColor = AppColors.Border,
                    unfocusedBorderColor = AppColors.Border,
                )
            )

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(AppDimens.CornerMediumSmall)),
                color = AppColors.Surface,
                border = BorderStroke(AppDimens.CardBorderWidth, AppColors.Border),
                shape = RoundedCornerShape(AppDimens.CornerMediumSmall)
            ) {
                LazyColumn {
                    itemsIndexed(filteredMedications) { index, medication ->
                        MedicationListItem(
                            medication = medication,
                            onClick = { onMedicationSelected(medication) },
                            onInfoClick = { color ->
                                focusManager.clearFocus()
                                viewModel.onInfoClick(medication, color)
                            }
                        )
                        if (index < filteredMedications.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = AppDimens.SpacingMedium),
                                thickness = AppDimens.CardBorderWidth / 2,
                                color = AppColors.Border
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(AppDimens.SpacingMedium))
        }
    }
}

@Composable
@Preview
fun ChooseMedicationScreenPreview() {
    MedCalculatorAppTheme {
    }
}
