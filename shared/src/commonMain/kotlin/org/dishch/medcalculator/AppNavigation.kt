package org.dishch.medcalculator

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.dishch.medcalculator.domain.CalculationResults
import org.dishch.medcalculator.ui.screens.results.CalculationResultsScreen
import org.dishch.medcalculator.ui.screens.choose.ChooseMedicationScreen
import org.dishch.medcalculator.ui.screens.main.MainScreen
import androidx.navigation.toRoute
import org.dishch.medcalculator.domain.CalculationResultType
import org.koin.compose.viewmodel.koinViewModel
import org.dishch.medcalculator.ui.screens.main.MainViewModel
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
    val mainViewModel: MainViewModel = koinViewModel()

    NavHost(
        navController = navController,
        startDestination = MainRoute,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { fadeOut(animationSpec = tween(200)) }
    ) {
        composable<MainRoute> {
            MainScreen(
                onChooseMedication = {
                    navController.navigate(ChooseMedicationRoute)
                },
                onCalculate = { result ->
                    navController.navigate(CalculationResultsRoute(result))
                },
                viewModel = mainViewModel
            )
        }
        composable<ChooseMedicationRoute> {
            ChooseMedicationScreen(
                onMedicationSelected = { medication ->
                    mainViewModel.onMedicationChanged(medication)
                    navController.navigateUp()
                },
                onBack = {
                    navController.navigateUp()
                }
            )
        }
        composable<CalculationResultsRoute>(
            typeMap = mapOf(typeOf<CalculationResults>() to CalculationResultType)
        ) { backStackEntry ->
            val route = backStackEntry.toRoute<CalculationResultsRoute>()
            CalculationResultsScreen(
                result = route.result,
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
