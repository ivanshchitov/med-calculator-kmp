package org.dishch.medcalculator.ui.theme

import androidx.compose.runtime.Composable

import platform.UIKit.*
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

@Composable
actual fun PlatformTheme(
    darkTheme: Boolean,
    content: @Composable (() -> Unit)
) {
    dispatch_async(dispatch_get_main_queue()) {
        val style: UIStatusBarStyle =
            if (!darkTheme) UIStatusBarStyleDarkContent else UIStatusBarStyleLightContent
        // Requires Info.plist: UIViewControllerBasedStatusBarAppearance = false
        UIApplication.sharedApplication.setStatusBarStyle(style, animated = true)
    }
    content()
}