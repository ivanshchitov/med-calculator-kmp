package org.dishch.medcalculator.ui.theme

import androidx.compose.ui.Modifier

/**
 * Extension to make a modifier conditional.
 * If the condition is true, the modifier will be applied, otherwise it won't.
 */
fun Modifier.conditional(condition: Boolean, modifier: Modifier.() -> Modifier): Modifier {
    return if (condition) {
        then(modifier(Modifier))
    } else {
        this
    }
}
