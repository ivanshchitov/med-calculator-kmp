package org.dishch.medcalculator

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.dishch.medcalculator.ui.screens.results.CalculationResultsRoute
import org.dishch.medcalculator.ui.screens.results.CalculationResultSerializer
import org.dishch.medcalculator.ui.screens.results.CalculationResultsScreen
import org.dishch.medcalculator.ui.screens.choose.ChooseMedicationScreen
import org.dishch.medcalculator.ui.screens.main.MainScreen
import androidx.navigation.toRoute
import org.koin.compose.viewmodel.koinViewModel
import org.dishch.medcalculator.ui.screens.main.MainViewModel

@Serializable
object MainRoute

@Serializable
object ChooseMedicationRoute

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
                    val serialized = CalculationResultSerializer.serialize(result)
                    navController.navigate(CalculationResultsRoute(serialized))
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
        composable<CalculationResultsRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<CalculationResultsRoute>()
            val result = CalculationResultSerializer.deserialize(route.serializedResult)
            CalculationResultsScreen(
                result = result,
                onBack = {
                    navController.navigateUp()
                }
            )
        }
    }
}
