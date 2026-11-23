package org.dishch.medcalculator.updates

import android.app.Activity
import android.content.Context
import android.util.Log
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.rustore.sdk.appupdate.listener.InstallStateUpdateListener
import ru.rustore.sdk.appupdate.manager.RuStoreAppUpdateManager
import ru.rustore.sdk.appupdate.manager.factory.RuStoreAppUpdateManagerFactory
import ru.rustore.sdk.appupdate.model.AppUpdateOptions
import ru.rustore.sdk.appupdate.model.AppUpdateType
import ru.rustore.sdk.appupdate.model.InstallState
import ru.rustore.sdk.appupdate.model.InstallStatus
import ru.rustore.sdk.appupdate.model.UpdateAvailability

private const val TAG = "RuStoreUpdate"

/**
 * Wraps RuStore In-app updates SDK for the delayed (flexible) update scenario.
 *
 * The SDK runs its own update dialogs, so no Activity reference is retained.
 * Check failures (RuStore missing, user unauthorized, app not published) are logged
 * only — per RuStore guidance the user is not shown an error.
 */
class RuStoreUpdateController(context: Context) {

    private val appContext = context.applicationContext
    private val manager: RuStoreAppUpdateManager by lazy {
        RuStoreAppUpdateManagerFactory.create(appContext)
    }

    private val _state = MutableStateFlow<UpdateState>(UpdateState.Idle)
    val state: StateFlow<UpdateState> = _state.asStateFlow()

    private var checkedOnce = false

    private val installStateListener = object : InstallStateUpdateListener {
        override fun onStateUpdated(state: InstallState) = handleInstallState(state)
    }

    init {
        // Kept registered while the Activity is stopped too: downloads may finish
        // while RuStore's own flow is in the foreground.
        manager.registerListener(installStateListener)
    }

    /** Performs the one-per-process update check. */
    fun checkForUpdateOnce() {
        if (checkedOnce) return
        checkedOnce = true
        checkForUpdate()
    }

    fun release() {
        manager.unregisterListener(installStateListener)
    }

    private fun checkForUpdate() {
        Log.d(TAG, "getAppUpdateInfo requested, installed versionCode=${installedVersionCode()}")
        manager.getAppUpdateInfo()
            .addOnSuccessListener { info ->
                Log.d(
                    TAG,
                    "getAppUpdateInfo: availability=${info.updateAvailability}, " +
                        "installStatus=${info.installStatus}, " +
                        "availableVersionCode=${info.availableVersionCode}"
                )
                _state.value = when {
                    info.updateAvailability != UpdateAvailability.UPDATE_AVAILABLE -> UpdateState.Idle
                    info.installStatus == InstallStatus.DOWNLOADED -> UpdateState.ReadyToInstall
                    else -> UpdateState.Available
                }
            }
            .addOnFailureListener { throwable ->
                Log.w(TAG, "getAppUpdateInfo failed", throwable)
                _state.value = UpdateState.Idle
            }
    }

    @Suppress("DEPRECATION")
    private fun installedVersionCode(): Long = runCatching {
        appContext.packageManager.getPackageInfo(appContext.packageName, 0).longVersionCode
    }.getOrDefault(-1L)

    /** Starts the flexible update flow: RuStore asks for confirmation, then downloads. */
    fun download() {
        // AppUpdateInfo becomes invalid after a single use, so always request a fresh one.
        manager.getAppUpdateInfo()
            .addOnSuccessListener { info ->
                if (info.updateAvailability != UpdateAvailability.UPDATE_AVAILABLE) {
                    _state.value = UpdateState.Idle
                    return@addOnSuccessListener
                }

                manager.startUpdateFlow(
                    info,
                    AppUpdateOptions.Builder()
                        .appUpdateType(AppUpdateType.FLEXIBLE)
                        .build()
                )
                    .addOnSuccessListener { resultCode ->
                        when (resultCode) {
                            Activity.RESULT_CANCELED -> _state.value = UpdateState.Idle
                            Activity.RESULT_OK -> {
                                if (_state.value !is UpdateState.Downloading) {
                                    _state.value = UpdateState.Downloading(0, 0)
                                }
                            }
                        }
                    }
                    .addOnFailureListener { throwable ->
                        Log.w(TAG, "startUpdateFlow failed", throwable)
                        _state.value = UpdateState.Available
                    }
            }
            .addOnFailureListener { throwable ->
                Log.w(TAG, "getAppUpdateInfo failed", throwable)
                _state.value = UpdateState.Idle
            }
    }

    /** Finishes a downloaded update. The app restarts on success. */
    fun install() {
        _state.value = UpdateState.Installing
        manager.completeUpdate(
            AppUpdateOptions.Builder()
                .appUpdateType(AppUpdateType.FLEXIBLE)
                .build()
        ).addOnFailureListener { throwable ->
            Log.w(TAG, "completeUpdate failed", throwable)
            _state.value = UpdateState.ReadyToInstall
        }
    }

    private fun handleInstallState(state: InstallState) {
        when (state.installStatus) {
            InstallStatus.DOWNLOADING -> {
                _state.value = UpdateState.Downloading(
                    bytesDownloaded = state.bytesDownloaded,
                    totalBytes = state.totalBytesToDownload
                )
            }

            InstallStatus.DOWNLOADED -> _state.value = UpdateState.ReadyToInstall
            InstallStatus.INSTALLING -> _state.value = UpdateState.Installing

            InstallStatus.FAILED -> {
                if (_state.value is UpdateState.Downloading) {
                    _state.value = UpdateState.Available
                }
            }

            InstallStatus.DOWNLOAD_INTERRUPTED -> {
                if (_state.value is UpdateState.Downloading) {
                    _state.value = UpdateState.Idle
                }
            }
        }
    }
}
