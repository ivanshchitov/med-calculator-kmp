package org.dishch.medcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import org.dishch.medcalculator.updates.RuStoreUpdateController
import org.dishch.medcalculator.updates.UpdateBanner

class MainActivity : ComponentActivity() {

    private val updateController by lazy { RuStoreUpdateController(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            val updateState by updateController.state.collectAsState()

            Box(modifier = Modifier.fillMaxSize()) {
                App()
                UpdateBanner(
                    state = updateState,
                    onDownload = updateController::download,
                    onInstall = updateController::install
                )
            }
        }
    }

    override fun onStart() {
        super.onStart()
        updateController.checkForUpdateOnce()
    }

    override fun onDestroy() {
        updateController.release()
        super.onDestroy()
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}