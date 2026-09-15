package com.example.data.admob

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.MobileAds

object AdMobManager {
    private const val TAG = "AdMobManager"

    // Official user AdMob IDs
    const val ADMOB_APP_ID = "ca-app-pub-4880243637225183~4956380952"
    const val PUBLISHER_ID = "pub-4880243637225183"

    // Standard Google test ad unit ID for banners (prevents test policy violations)
    const val TEST_BANNER_AD_UNIT_ID = "ca-app-pub-3940256099942544/6300978111"

    var activeBannerAdUnitId: String = TEST_BANNER_AD_UNIT_ID

    @Volatile
    var isInitialized: Boolean = false
        private set

    @Volatile
    var isAvailable: Boolean = false
        private set

    fun initialize(context: Context) {
        if (isInitialized) return

        try {
            val appContext = context.applicationContext ?: context
            MobileAds.initialize(appContext) { status ->
                isInitialized = true
                isAvailable = true
                Log.d(TAG, "AdMob MobileAds initialized successfully: $status")
            }
            isAvailable = true
        } catch (t: Throwable) {
            Log.w(TAG, "AdMob initialization encountered error (running in safe offline/fallback mode): ${t.message}")
            isInitialized = false
            isAvailable = false
        }
    }
}
