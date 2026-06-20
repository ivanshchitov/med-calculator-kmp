package org.dishch.medcalculator

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.dishch.medcalculator.domain.CalculationResults
import org.dishch.medcalculator.domain.MedicationUi
import org.dishch.medcalculator.ui.screens.CalculationResultsScreen
import org.dishch.medcalculator.ui.screens.ChooseMedicationScreen
import org.dishch.medcalculator.ui.screens.MainScreen
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
        mutableStateOf(MedicationUi("Парацетамол", "120 мг/мл"))
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
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
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
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
