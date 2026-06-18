package org.dishch.medcalculator

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import org.dishch.medcalculator.ui.screens.MainScreen
import org.dishch.medcalculator.ui.theme.MedCalculatorAppTheme

@Composable
@Preview
fun App() {
    MedCalculatorAppTheme {
        MainScreen()
    }
}
