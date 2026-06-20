package org.dishch.medcalculator

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.dishch.medcalculator.domain.MedicationUi
import org.dishch.medcalculator.ui.screens.ChooseMedicationScreen
import org.dishch.medcalculator.ui.screens.MainScreen

@Serializable
object Main

@Serializable
object ChooseMedication

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var selectedMedication by remember {
        mutableStateOf(MedicationUi("Парацетамол", "120 мг/мл"))
    }

    NavHost(
        navController = navController,
        startDestination = Main
    ) {
        composable<Main> {
            MainScreen(
                selectedMedication = selectedMedication,
                onChooseMedication = {
                    navController.navigate(ChooseMedication)
                }
            )
        }
        composable<ChooseMedication> {
            ChooseMedicationScreen(
                onMedicationSelected = { medication ->
                    selectedMedication = medication
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
