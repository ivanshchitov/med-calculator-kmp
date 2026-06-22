package org.dishch.medcalculator

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.dishch.medcalculator.domain.CalculationResults
import org.dishch.medcalculator.domain.Medication
import org.dishch.medcalculator.ui.screens.results.CalculationResultsScreen
import org.dishch.medcalculator.ui.screens.choose.ChooseMedicationScreen
import org.dishch.medcalculator.ui.screens.main.MainScreen
import androidx.navigation.toRoute
import org.dishch.medcalculator.domain.CalculationResultType
import kotlin.reflect.typeOf

@Serializable
object MainRoute

@Serializable
object ChooseMedicationRoute

@Serializable
data class CalculationResultsRoute(
    val result: CalculationResults
)

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    var selectedMedication by remember {
        mutableStateOf(Medication(0, "Парацетамол", 120.0, 0.0, 0))
    }

    NavHost(
        navController = navController,
        startDestination = MainRoute
    ) {
        composable<MainRoute> {
            MainScreen(
                selectedMedication = selectedMedication,
                onChooseMedication = {
                    navController.navigate(ChooseMedicationRoute)
                },
                onCalculate = { result ->
                    navController.navigate(CalculationResultsRoute(result))
                }
            )
        }
        composable<ChooseMedicationRoute> {
            ChooseMedicationScreen(
                onMedicationSelected = { medication ->
                    selectedMedication = medication
                    if (navController.currentDestination?.route?.contains("ChooseMedicationRoute") == true) {
                        navController.popBackStack()
                    }
                },
                onBack = {
                    if (navController.currentDestination?.route?.contains("ChooseMedicationRoute") == true) {
                        navController.popBackStack()
                    }
                }
            )
        }
        composable<CalculationResultsRoute>(
            typeMap = mapOf(typeOf<CalculationResults>() to CalculationResultType)
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<CalculationResultsRoute>()
            CalculationResultsScreen(
                result = route.result,
                onNewCalculation = {
                    if (navController.currentDestination?.route?.contains("CalculationResultsRoute") == true) {
                        navController.popBackStack()
                    }
                },
                onBack = {
                    if (navController.currentDestination?.route?.contains("CalculationResultsRoute") == true) {
                        navController.popBackStack()
                    }
                }
            )
        }
    }
}
