package com.example.data.admob

import android.content.Context
import android.os.Build
import android.util.Log
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object AdMobManager {
    private const val TAG = "AdMobManager"

    // Official user AdMob IDs
    const val ADMOB_APP_ID = "ca-app-pub-4880243637225183~4956380952"
    const val PUBLISHER_ID = "pub-4880243637225183"
    const val APP_ADS_TXT_RECORD = "google.com, pub-4880243637225183, DIRECT, f08c47fec0942fa0"

    // Standard Google test ad unit ID for banners (prevents test policy violations)
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    var activeBannerAdUnitId: String = TEST_BANNER_AD_UNIT_ID

    fun copyAppAdsTxtSnippet(context: Context): Boolean {
        return try {
            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
            val clip = android.content.ClipData.newPlainText("app-ads.txt", APP_ADS_TXT_RECORD)
            clipboard.setPrimaryClip(clip)
            true
        } catch (_: Throwable) {
            false
        }
    }

    fun shareAppAdsTxt(context: Context) {
        try {
            val intent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(android.content.Intent.EXTRA_SUBJECT, "Balasore 360 app-ads.txt record")
                putExtra(android.content.Intent.EXTRA_TEXT, APP_ADS_TXT_RECORD)
            }
            context.startActivity(android.content.Intent.createChooser(intent, "Share app-ads.txt snippet"))
        } catch (_: Throwable) {}
    }

    @Volatile
    var isInitialized: Boolean = false
        private set

    @Volatile
    var isAvailable: Boolean = false
        private set

    fun initialize(context: Context) {
        if (isInitialized) return

        val appContext = context.applicationContext ?: context

        // Detect emulator environment to prevent measurement service and Mesa rendernode errors
        val isEmulator = (
            Build.FINGERPRINT.startsWith("generic")
            || Build.FINGERPRINT.startsWith("unknown")
            || Build.MODEL.contains("google_sdk")
            || Build.MODEL.contains("Emulator")
            || Build.MODEL.contains("Android SDK built for x86")
            || Build.MANUFACTURER.contains("Genymotion")
            || Build.HARDWARE.contains("goldfish")
            || Build.HARDWARE.contains("ranchu")
            || Build.PRODUCT.contains("sdk_gphone")
            || Build.PRODUCT.contains("google_sdk")
            || Build.PRODUCT.contains("emulator")
            || Build.PRODUCT.contains("simulator")
            || Build.BOARD.lowercase().contains("goldfish")
            || Build.BOARD.lowercase().contains("ranchu")
        )

        if (isEmulator) {
            Log.i(TAG, "Running in emulator environment. AdMob Live View paused to prevent adservices binding and Mesa rendernode logs.")
            isAvailable = false
            isInitialized = true
            return
        }

        // Verify Google Play Services is available on physical devices
        try {
            val availability = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(appContext)
            if (availability != ConnectionResult.SUCCESS) {
                Log.i(TAG, "Google Play Services not ready ($availability). Operating in safe sponsor fallback mode.")
                isAvailable = false
                isInitialized = true
                return
            }
        } catch (t: Throwable) {
            Log.w(TAG, "Play Services check caught: ${t.message}. Safe fallback active.")
            isAvailable = false
            isInitialized = true
            return
        }

        // Initialize asynchronously on IO dispatcher to prevent main thread blocking
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reqConfig = RequestConfiguration.Builder()
                    .setTestDeviceIds(listOf(AdRequest.DEVICE_ID_EMULATOR))
                    .build()
                MobileAds.setRequestConfiguration(reqConfig)

                MobileAds.initialize(appContext) { status ->
                    isInitialized = true
                    isAvailable = true
                    Log.d(TAG, "AdMob MobileAds initialized successfully: $status")
                }
            } catch (t: Throwable) {
                Log.w(TAG, "AdMob init error: ${t.message}. Falling back gracefully.")
                isAvailable = false
                isInitialized = true
            }
        }
    }
}
