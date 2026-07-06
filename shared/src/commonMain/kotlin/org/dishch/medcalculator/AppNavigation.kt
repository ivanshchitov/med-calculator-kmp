package org.dishch.medcalculator

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.*
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable
import org.dishch.medcalculator.ui.screens.choose.ChooseMedicationScreen
import org.dishch.medcalculator.ui.screens.main.MainScreen
import org.dishch.medcalculator.ui.screens.main.MainViewModel
import org.dishch.medcalculator.ui.screens.results.CalculationResultsRoute
import org.dishch.medcalculator.ui.screens.results.CalculationResultsScreen
import org.dishch.medcalculator.ui.screens.results.CalculationResultSerializer
import org.koin.compose.viewmodel.koinViewModel

@Serializable
object MainRoute

@Serializable
object ChooseMedicationRoute

@Composable
private fun getMainViewModel(navController: NavController): MainViewModel {
    val backStackEntry = navController.currentBackStackEntry
    val parentEntry = backStackEntry?.let { entry ->
        remember(entry) { navController.getBackStackEntry(MainRoute) }
    } ?: navController.getBackStackEntry(MainRoute)
    return koinViewModel<MainViewModel>(viewModelStoreOwner = parentEntry)
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = MainRoute,
        enterTransition = { fadeIn(animationSpec = tween(200)) },
        exitTransition = { fadeOut(animationSpec = tween(200)) },
        popEnterTransition = { fadeIn(animationSpec = tween(200)) },
        popExitTransition = { fadeOut(animationSpec = tween(200)) }
    ) {
        composable<MainRoute> {
            val mainViewModel = getMainViewModel(navController)
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
            val mainViewModel = getMainViewModel(navController)
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
