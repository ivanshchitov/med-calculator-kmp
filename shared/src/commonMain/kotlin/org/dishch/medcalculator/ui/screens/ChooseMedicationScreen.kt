package org.dishch.medcalculator.ui.screens

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
import androidx.compose.ui.unit.dp
import medcalculator.shared.generated.resources.Res
import medcalculator.shared.generated.resources.choose_medication
import medcalculator.shared.generated.resources.search
import org.dishch.medcalculator.domain.MedicationUi
import org.dishch.medcalculator.ui.components.MedicationListItem
import org.dishch.medcalculator.ui.theme.AppColors
import org.dishch.medcalculator.ui.theme.AppDimens
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChooseMedicationScreen(
    onMedicationSelected: (MedicationUi) -> Unit,
    onBack: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val medications = remember {
        listOf(
            MedicationUi("Парацетамол", "120 мг/мл"),
            MedicationUi("Ибупрофен", "100 мг/мл"),
            MedicationUi("Амоксициллин", "250 мг/мл"),
            MedicationUi("Цефтриаксон", "1000 мг/мл"),
            MedicationUi("Дексаметазон", "4 мг/мл"),
            MedicationUi("Сальбутамол", "2 мг/мл"),
            MedicationUi("Но-шпа", "40 мг/мл"),
            MedicationUi("Метоклопрамид", "5 мг/мл"),
            MedicationUi("Преднизолон", "5 мг/мл"),
            MedicationUi("Лоратадин", "10 мг/мл")
        )
    }

    val filteredMedications = medications.filter {
        it.name.startsWith(searchQuery, ignoreCase = true)
    }

    val focusManager = LocalFocusManager.current

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
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(horizontal = 16.dp)
                .padding(bottom = 16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                placeholder = { Text(stringResource(Res.string.search)) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                shape = RoundedCornerShape(AppDimens.CardCorner),
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
                    .clip(RoundedCornerShape(AppDimens.CardCorner)),
                color = AppColors.Surface,
                border = BorderStroke(2.dp, AppColors.Border),
                shape = RoundedCornerShape(AppDimens.CardCorner)
            ) {
                LazyColumn {
                    itemsIndexed(filteredMedications) { index, medication ->
                        MedicationListItem(
                            medication = medication,
                            onClick = { onMedicationSelected(medication) }
                        )
                        if (index < filteredMedications.lastIndex) {
                            HorizontalDivider(
                                modifier = Modifier.padding(horizontal = 16.dp),
                                thickness = 1.dp,
                                color = AppColors.Border
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
@Preview
fun ChooseMedicationScreenPreview() {
    MedCalculatorAppTheme {
        ChooseMedicationScreen(
            onMedicationSelected = {},
            onBack = {}
        )
    }
}
