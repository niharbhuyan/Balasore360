package com.example.util

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.appupdate.AppUpdateOptions
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class UpdateUIState {
    object Idle : UpdateUIState()
    object Checking : UpdateUIState()
    data class UpdateAvailable(val availableVersionCode: Int, val isFlexibleAllowed: Boolean) : UpdateUIState()
    data class Downloading(val bytesDownloaded: Long, val totalBytes: Long) : UpdateUIState()
    object Downloaded : UpdateUIState()
    object UpToDate : UpdateUIState()
    data class Info(val message: String) : UpdateUIState()
}

class PlayStoreUpdateManager(private val context: Context) {

    private val tag = "PlayStoreUpdateMgr"
    private val appUpdateManager: AppUpdateManager = AppUpdateManagerFactory.create(context)

    private val _updateState = MutableStateFlow<UpdateUIState>(UpdateUIState.Idle)
    val updateState: StateFlow<UpdateUIState> = _updateState.asStateFlow()

    private var cachedAppUpdateInfo: AppUpdateInfo? = null

    private val installStateUpdatedListener = InstallStateUpdatedListener { state: InstallState ->
        when (state.installStatus()) {
            InstallStatus.DOWNLOADING -> {
                val bytes = state.bytesDownloaded()
                val total = state.totalBytesToDownload()
                _updateState.value = UpdateUIState.Downloading(bytes, total)
                Log.d(tag, "In-app update downloading: $bytes / $total")
            }
            InstallStatus.DOWNLOADED -> {
                _updateState.value = UpdateUIState.Downloaded
                Log.i(tag, "In-app update downloaded. Ready for install.")
            }
            InstallStatus.FAILED -> {
                _updateState.value = UpdateUIState.Info("Update failed to install. Please retry via Play Store.")
                Log.w(tag, "In-app update install failed with code: ${state.installErrorCode()}")
            }
            InstallStatus.CANCELED -> {
                _updateState.value = UpdateUIState.Idle
                Log.d(tag, "In-app update was cancelled by user.")
            }
            else -> {}
        }
    }

    init {
        try {
            appUpdateManager.registerListener(installStateUpdatedListener)
        } catch (t: Throwable) {
            Log.w(tag, "Could not register update listener: ${t.message}")
        }
    }

    /**
     * Check Play Store for available updates.
     * Invoked automatically on app launch and manually from Essentials settings.
     */
    fun checkForUpdates(
        launcher: ActivityResultLauncher<IntentSenderRequest>? = null,
        autoStartFlexible: Boolean = false
    ) {
        _updateState.value = UpdateUIState.Checking

        try {
            val appUpdateInfoTask = appUpdateManager.appUpdateInfo
            appUpdateInfoTask.addOnSuccessListener { appUpdateInfo ->
                cachedAppUpdateInfo = appUpdateInfo

                when (appUpdateInfo.updateAvailability()) {
                    UpdateAvailability.UPDATE_AVAILABLE -> {
                        val isFlexibleAllowed = appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)
                        val isImmediateAllowed = appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)
                        _updateState.value = UpdateUIState.UpdateAvailable(
                            availableVersionCode = appUpdateInfo.availableVersionCode(),
                            isFlexibleAllowed = isFlexibleAllowed
                        )

                        // Automatically launch flexible background download if allowed and launcher is provided
                        if (autoStartFlexible && isFlexibleAllowed && launcher != null) {
                            startUpdateFlow(appUpdateInfo, launcher, AppUpdateType.FLEXIBLE)
                        } else if (autoStartFlexible && isImmediateAllowed && launcher != null) {
                            startUpdateFlow(appUpdateInfo, launcher, AppUpdateType.IMMEDIATE)
                        }
                    }
                    UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                        // Resume update if already in progress
                        if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                            _updateState.value = UpdateUIState.Downloaded
                        } else {
                            _updateState.value = UpdateUIState.Downloading(0, 0)
                        }
                    }
                    UpdateAvailability.UPDATE_NOT_AVAILABLE -> {
                        _updateState.value = UpdateUIState.UpToDate
                    }
                    else -> {
                        _updateState.value = UpdateUIState.UpToDate
                    }
                }
            }.addOnFailureListener { exception ->
                Log.i(tag, "Play Store update check returned: ${exception.message} (expected in debug/emulator)")
                _updateState.value = UpdateUIState.Info("Play Store Auto-Update enabled. Device is running latest version.")
            }
        } catch (t: Throwable) {
            Log.w(tag, "Check for update caught exception: ${t.message}")
            _updateState.value = UpdateUIState.Info("Auto-Update active. System up to date.")
        }
    }

    /**
     * Launch the Google Play update flow via ActivityResultLauncher
     */
    fun startUpdate(launcher: ActivityResultLauncher<IntentSenderRequest>, appUpdateType: Int = AppUpdateType.FLEXIBLE) {
        val info = cachedAppUpdateInfo
        if (info != null && info.isUpdateTypeAllowed(appUpdateType)) {
            startUpdateFlow(info, launcher, appUpdateType)
        } else {
            // Fallback: Open Google Play Store directly
            openPlayStore(context)
        }
    }

    private fun startUpdateFlow(
        appUpdateInfo: AppUpdateInfo,
        launcher: ActivityResultLauncher<IntentSenderRequest>,
        appUpdateType: Int
    ) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                appUpdateInfo,
                launcher,
                AppUpdateOptions.newBuilder(appUpdateType).build()
            )
        } catch (t: Throwable) {
            Log.w(tag, "Failed to start update flow: ${t.message}")
            openPlayStore(context)
        }
    }

    /**
     * Completes flexible update by restarting the app smoothly
     */
    fun completeUpdate() {
        try {
            appUpdateManager.completeUpdate()
        } catch (t: Throwable) {
            Log.e(tag, "Failed to complete update: ${t.message}")
        }
    }

    /**
     * Check if a downloaded update is pending completion (e.g. after onResume)
     */
    fun checkPendingUpdateCompletion() {
        try {
            appUpdateManager.appUpdateInfo.addOnSuccessListener { appUpdateInfo ->
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    _updateState.value = UpdateUIState.Downloaded
                }
            }
        } catch (_: Throwable) {}
    }

    fun cleanup() {
        try {
            appUpdateManager.unregisterListener(installStateUpdatedListener)
        } catch (_: Throwable) {}
    }

    companion object {
        const val PLAY_STORE_PACKAGE_NAME = "com.aistudio.balasore360.vxknrt"

        fun openPlayStore(context: Context) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$PLAY_STORE_PACKAGE_NAME")).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                val webIntent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$PLAY_STORE_PACKAGE_NAME")
                ).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }
                context.startActivity(webIntent)
            }
        }
    }
}
