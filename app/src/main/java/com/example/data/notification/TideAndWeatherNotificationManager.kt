package com.example.data.notification

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.example.MainActivity
import com.example.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.TimeUnit

/**
 * Central Manager for the local notification system in Balasore 360.
 * Handles:
 * 1. Chandipur Beach High Tide warnings and sea surge alerts.
 * 2. Real-time Balasore weather updates and severe meteorological alerts.
 * 3. WorkManager background periodic polling and execution triggers.
 */
object TideAndWeatherNotificationManager {

    private const val TAG = "TideWeatherNotify"
    private const val PREFS_NAME = "tide_weather_notification_prefs"

    // Notification Channel IDs
    const val CHANNEL_ID_TIDE = "chandipur_high_tide_channel"
    const val CHANNEL_NAME_TIDE = "Chandipur Beach High Tide Warnings"

    const val CHANNEL_ID_WALK_WINDOW = "chandipur_vanishing_sea_walk_channel"
    const val CHANNEL_NAME_WALK_WINDOW = "Chandipur Vanishing Sea Walk Alerts"

    const val CHANNEL_ID_WEATHER = "balasore_weather_updates_channel"
    const val CHANNEL_NAME_WEATHER = "Balasore Weather Alerts & Updates"

    // Base Notification IDs
    const val NOTIFICATION_ID_HIGH_TIDE = 8101
    const val NOTIFICATION_ID_WALK_WINDOW_OPEN = 8110
    const val NOTIFICATION_ID_WALK_WINDOW_CLOSE = 8120
    const val NOTIFICATION_ID_WEATHER = 8201

    // Preference Keys
    private const val KEY_TIDE_ALERTS_ENABLED = "key_tide_alerts_enabled"
    private const val KEY_WALK_ALERTS_ENABLED = "key_walk_alerts_enabled"
    private const val KEY_WALK_ADVANCE_MINUTES = "key_walk_advance_minutes"
    private const val KEY_ADVANCE_WARNING_MINUTES = "key_advance_warning_minutes"
    private const val KEY_WEATHER_ALERTS_ENABLED = "key_weather_alerts_enabled"
    private const val KEY_LAST_TIDE_NOTIFY_TIME = "key_last_tide_notify_time"
    private const val KEY_LAST_WALK_NOTIFY_TIME = "key_last_walk_notify_time"
    private const val KEY_LAST_WEATHER_NOTIFY_TIME = "key_last_weather_notify_time"
    private const val KEY_LAST_TIDE_STATE_NOTIFIED = "key_last_tide_state_notified"
    private const val KEY_LAST_WALK_STATE_NOTIFIED = "key_last_walk_state_notified"

    private fun getPrefs(context: Context): SharedPreferences {
        return context.applicationContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    // === User Preferences ===

    fun isWalkWindowAlertsEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_WALK_ALERTS_ENABLED, true)
    }

    fun setWalkWindowAlertsEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_WALK_ALERTS_ENABLED, enabled).apply()
    }

    fun getWalkWindowAdvanceMinutes(context: Context): Int {
        return getPrefs(context).getInt(KEY_WALK_ADVANCE_MINUTES, 30)
    }

    fun setWalkWindowAdvanceMinutes(context: Context, minutes: Int) {
        getPrefs(context).edit().putInt(KEY_WALK_ADVANCE_MINUTES, minutes).apply()
    }

    fun getLastWalkNotifyTime(context: Context): Long {
        return getPrefs(context).getLong(KEY_LAST_WALK_NOTIFY_TIME, 0L)
    }

    fun setLastWalkNotifyTime(context: Context, time: Long) {
        getPrefs(context).edit().putLong(KEY_LAST_WALK_NOTIFY_TIME, time).apply()
    }

    fun getLastWalkStateNotified(context: Context): String {
        return getPrefs(context).getString(KEY_LAST_WALK_STATE_NOTIFIED, "") ?: ""
    }

    fun setLastWalkStateNotified(context: Context, state: String) {
        getPrefs(context).edit().putString(KEY_LAST_WALK_STATE_NOTIFIED, state).apply()
    }

    fun isTideAlertsEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_TIDE_ALERTS_ENABLED, true)
    }

    fun setTideAlertsEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_TIDE_ALERTS_ENABLED, enabled).apply()
    }

    fun getAdvanceWarningMinutes(context: Context): Int {
        return getPrefs(context).getInt(KEY_ADVANCE_WARNING_MINUTES, 30)
    }

    fun setAdvanceWarningMinutes(context: Context, minutes: Int) {
        getPrefs(context).edit().putInt(KEY_ADVANCE_WARNING_MINUTES, minutes).apply()
    }

    fun isWeatherAlertsEnabled(context: Context): Boolean {
        return getPrefs(context).getBoolean(KEY_WEATHER_ALERTS_ENABLED, true)
    }

    fun setWeatherAlertsEnabled(context: Context, enabled: Boolean) {
        getPrefs(context).edit().putBoolean(KEY_WEATHER_ALERTS_ENABLED, enabled).apply()
    }

    fun getLastTideNotifyTime(context: Context): Long {
        return getPrefs(context).getLong(KEY_LAST_TIDE_NOTIFY_TIME, 0L)
    }

    fun setLastTideNotifyTime(context: Context, time: Long) {
        getPrefs(context).edit().putLong(KEY_LAST_TIDE_NOTIFY_TIME, time).apply()
    }

    fun getLastWeatherNotifyTime(context: Context): Long {
        return getPrefs(context).getLong(KEY_LAST_WEATHER_NOTIFY_TIME, 0L)
    }

    fun setLastWeatherNotifyTime(context: Context, time: Long) {
        getPrefs(context).edit().putLong(KEY_LAST_WEATHER_NOTIFY_TIME, time).apply()
    }

    fun getLastTideStateNotified(context: Context): String {
        return getPrefs(context).getString(KEY_LAST_TIDE_STATE_NOTIFIED, "") ?: ""
    }

    fun setLastTideStateNotified(context: Context, state: String) {
        getPrefs(context).edit().putString(KEY_LAST_TIDE_STATE_NOTIFIED, state).apply()
    }

    // === Notification Channels Setup ===

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

            // 1. Chandipur High Tide Warning Channel
            val tideChannel = NotificationChannel(
                CHANNEL_ID_TIDE,
                CHANNEL_NAME_TIDE,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical alerts when the Bay of Bengal sea returns to Chandipur beach. Warns visitors to return to shore before high tide."
                enableLights(true)
                lightColor = Color.rgb(2, 132, 199) // Ocean Blue
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 450, 200, 450, 200, 450)
            }

            // 2. Chandipur Vanishing Sea Walk Window Channel (Opening & Closing alerts)
            val walkChannel = NotificationChannel(
                CHANNEL_ID_WALK_WINDOW,
                CHANNEL_NAME_WALK_WINDOW,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Proactive push alerts when the Chandipur 'vanishing sea' walk window opens as sea recedes, and countdown return sirens before it closes."
                enableLights(true)
                lightColor = Color.rgb(16, 185, 129) // Emerald Sea Green
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 500, 250, 500, 250, 500)
            }

            // 3. Weather Updates Channel
            val weatherChannel = NotificationChannel(
                CHANNEL_ID_WEATHER,
                CHANNEL_NAME_WEATHER,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Real-time updates on Balasore weather, Kalbaisakhi squalls, rain forecasts, and cyclone warnings."
                enableLights(true)
                lightColor = Color.rgb(249, 115, 22) // Amber Orange
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 300, 150, 300)
            }

            notificationManager.createNotificationChannel(tideChannel)
            notificationManager.createNotificationChannel(walkChannel)
            notificationManager.createNotificationChannel(weatherChannel)
        }
    }

    private fun hasNotificationPermission(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
    }

    // === Notification Dispatch Methods ===

    /**
     * Dispatches an imminent High Tide Warning alert for Chandipur Beach.
     * Triggered when high tide is arriving within the advance warning threshold.
     */
    fun showHighTideWarning(
        context: Context,
        minutesRemaining: Int,
        tideHeightMeters: Double,
        isUrgent: Boolean = false,
        customTitle: String? = null,
        customMessage: String? = null
    ) {
        if (!isTideAlertsEnabled(context)) return
        createNotificationChannels(context)
        if (!hasNotificationPermission(context)) return

        val title = customTitle ?: if (isUrgent) {
            "🚨 URGENT: Chandipur High Tide Returning in ${minutesRemaining}m"
        } else {
            "⚠️ Chandipur High Tide Warning (${minutesRemaining}m Remaining)"
        }

        val message = customMessage ?: if (isUrgent) {
            "The sea is surging back rapidly across the 5 km intertidal mudflats. Safe walk window is CLOSED. Evacuate mudflats to Chandipur promenade immediately!"
        } else {
            "Bay of Bengal sea has commenced its return cycle. Expected high tide height: %.2fm. Visitors walking on the seabed should turn back toward the shore.".format(tideHeightMeters)
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "WEATHER")
            putExtra("open_feature_sheet", "CHANDIPUR_TIDE")
            putExtra("tide_alert", true)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_HIGH_TIDE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Action intent to open the Offline Map of Cyclone Shelters & Coast
        val mapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "ESSENTIALS")
            putExtra("open_feature_sheet", "BALASORE_MAP_EXPLORER")
        }
        val mapPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_HIGH_TIDE + 1,
            mapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_TIDE)
            .setSmallIcon(R.drawable.ic_notification_tide)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setBigContentTitle(title)
                    .setSummaryText("🌊 Chandipur Tidal Warning • Safe Return Notice")
            )
            .setColor(if (isUrgent) Color.rgb(220, 38, 38) else Color.rgb(2, 132, 199))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_notification_tide, "View Tide Radar", pendingIntent)
            .addAction(R.drawable.ic_notification_weather, "Coast & Shelter Map", mapPendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_HIGH_TIDE, notification)
            setLastTideNotifyTime(context, System.currentTimeMillis())
            Log.d(TAG, "Dispatched Chandipur High Tide Warning notification: $title")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to display High Tide notification", e)
        }
    }

    /**
     * Dispatches a Peak High Tide notice when the sea has fully reached the promenade.
     */
    fun showPeakHighTideAlert(
        context: Context,
        tideHeightMeters: Double,
        message: String = "Peak high tide active (%.2fm). Waves breaking along Chandipur sea embankment. Seabed access prohibited.".format(tideHeightMeters)
    ) {
        if (!isTideAlertsEnabled(context)) return
        createNotificationChannels(context)
        if (!hasNotificationPermission(context)) return

        val title = "🌊 Chandipur Peak High Tide Active (%.2fm)".format(tideHeightMeters)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "WEATHER")
            putExtra("open_feature_sheet", "CHANDIPUR_TIDE")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_HIGH_TIDE + 2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_TIDE)
            .setSmallIcon(R.drawable.ic_notification_tide)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setSummaryText("🌊 Chandipur Hydrographic Watch")
            )
            .setColor(Color.rgb(14, 116, 144))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_notification_tide, "Open Tide Tracker", pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_HIGH_TIDE, notification)
            setLastTideNotifyTime(context, System.currentTimeMillis())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to display peak high tide notification", e)
        }
    }

    /**
     * Proactively alerts users when the Chandipur 'vanishing sea' walk window is ABOUT TO OPEN.
     * Sent in advance (e.g. 15 to 30 mins) as the Bay of Bengal waters recede toward the low tide mark.
     */
    fun showWalkWindowAboutToOpen(
        context: Context,
        minutesUntilOpen: Int,
        distanceRecededKm: Double = 2.8,
        windowDurationHours: Double = 3.5
    ) {
        if (!isWalkWindowAlertsEnabled(context)) return
        createNotificationChannels(context)
        if (!hasNotificationPermission(context)) return

        val title = "🌊 Chandipur Vanishing Sea: Walk Window Opening in ${minutesUntilOpen}m"
        val message = "Bay of Bengal water has receded %.1f km! Safe seabed walk window opens in %d mins (~%.1fh duration). Ideal time to view living fossil horseshoe crabs & red ghost crab dunes.".format(
            distanceRecededKm,
            minutesUntilOpen,
            windowDurationHours
        )

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "WEATHER")
            putExtra("open_feature_sheet", "CHANDIPUR_TIDE")
            putExtra("walk_window_alert", "OPENING_SOON")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_WALK_WINDOW_OPEN,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val mapIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "HOTSPOTS")
            putExtra("open_feature_sheet", "TOURISM_HOTSPOTS")
        }
        val mapPendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_WALK_WINDOW_OPEN + 1,
            mapIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_WALK_WINDOW)
            .setSmallIcon(R.drawable.ic_notification_tide)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setBigContentTitle(title)
                    .setSummaryText("🦀 Chandipur Vanishing Sea • Opening Window")
            )
            .setColor(Color.rgb(16, 185, 129)) // Emerald Green
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_notification_tide, "Open Tide Radar", pendingIntent)
            .addAction(R.drawable.ic_notification_weather, "View Coast Map", mapPendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_WALK_WINDOW_OPEN, notification)
            setLastWalkNotifyTime(context, System.currentTimeMillis())
            Log.d(TAG, "Dispatched Walk Window About To Open notification: $title")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to display walk window opening notification", e)
        }
    }

    /**
     * Dispatches notification when sea recedes and safe walk window opens.
     */
    fun showWalkWindowOpened(
        context: Context,
        recededDistanceKm: Double,
        safeMinutesRemaining: Int
    ) {
        if (!isWalkWindowAlertsEnabled(context)) return
        createNotificationChannels(context)
        if (!hasNotificationPermission(context)) return

        val title = "🦀 Chandipur Vanishing Sea: Walk Window NOW OPEN!"
        val hours = safeMinutesRemaining / 60
        val mins = safeMinutesRemaining % 60
        val timeStr = if (hours > 0) "${hours}h ${mins}m" else "${mins}m"
        val message = "Sea has receded %.1f km into the Bay of Bengal! Safe walk window is now active for next $timeStr. Stay within safety flags (2.5 km perimeter).".format(recededDistanceKm)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "WEATHER")
            putExtra("open_feature_sheet", "CHANDIPUR_TIDE")
            putExtra("walk_window_alert", "OPEN_NOW")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_WALK_WINDOW_OPEN + 2,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_WALK_WINDOW)
            .setSmallIcon(R.drawable.ic_notification_tide)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setBigContentTitle(title)
                    .setSummaryText("🟢 Safe Seabed Stroll Active")
            )
            .setColor(Color.rgb(16, 185, 129)) // Emerald Green
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_notification_tide, "Explore Mudflats", pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_WALK_WINDOW_OPEN, notification)
            setLastWalkNotifyTime(context, System.currentTimeMillis())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to display safe walk notification", e)
        }
    }

    /**
     * Backward-compatible alias for showWalkWindowOpened.
     */
    fun showSafeWalkWindowOpen(
        context: Context,
        recededDistanceKm: Double,
        safeMinutesRemaining: Int
    ) {
        showWalkWindowOpened(context, recededDistanceKm, safeMinutesRemaining)
    }

    /**
     * Proactively alerts users when the Chandipur 'vanishing sea' walk window is ABOUT TO CLOSE.
     * Can trigger an advance caution (e.g. 30m prior) or an urgent return evacuation siren (e.g. 15m prior).
     */
    fun showWalkWindowAboutToClose(
        context: Context,
        minutesRemaining: Int,
        isUrgent: Boolean = false
    ) {
        if (!isWalkWindowAlertsEnabled(context)) return
        createNotificationChannels(context)
        if (!hasNotificationPermission(context)) return

        val title = if (isUrgent) {
            "🚨 URGENT: Chandipur Walk Window CLOSING in ${minutesRemaining}m!"
        } else {
            "⚠️ Chandipur Walk Window Closing in ${minutesRemaining}m: Return to Shore"
        }

        val message = if (isUrgent) {
            "Incoming high tide is rushing back rapidly across the flat seabed at ~0.8 m/s! Safe walk window closes in ${minutesRemaining} mins. Evacuate intertidal zone to the Chandipur promenade immediately!"
        } else {
            "Tidal reversal has begun in the Bay of Bengal. Water will commence filling seaward depressions. Begin walking back toward shore now (${minutesRemaining} mins safe buffer remaining)."
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "WEATHER")
            putExtra("open_feature_sheet", "CHANDIPUR_TIDE")
            putExtra("walk_window_alert", "CLOSING_SOON")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_WALK_WINDOW_CLOSE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_WALK_WINDOW)
            .setSmallIcon(R.drawable.ic_notification_tide)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setBigContentTitle(title)
                    .setSummaryText(if (isUrgent) "🚨 Urgent Shore Evacuation Alarm" else "⚠️ Safe Return Advisory")
            )
            .setColor(if (isUrgent) Color.rgb(220, 38, 38) else Color.rgb(249, 115, 22))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_notification_tide, "View Return Timer", pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_WALK_WINDOW_CLOSE, notification)
            setLastWalkNotifyTime(context, System.currentTimeMillis())
            Log.d(TAG, "Dispatched Walk Window About To Close notification: $title")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to display walk window closing notification", e)
        }
    }

    /**
     * Dispatches notification when the walk window has officially CLOSED and high tide has reached the shoreline.
     */
    fun showWalkWindowClosed(
        context: Context,
        tideHeightMeters: Double = 4.1
    ) {
        if (!isWalkWindowAlertsEnabled(context)) return
        createNotificationChannels(context)
        if (!hasNotificationPermission(context)) return

        val title = "⛔ Chandipur Walk Window CLOSED (High Tide %.2fm)".format(tideHeightMeters)
        val message = "Bay of Bengal has fully reclaimed the 5 km intertidal mudflats. Seabed access is now prohibited until the next receding tide cycle."

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "WEATHER")
            putExtra("open_feature_sheet", "CHANDIPUR_TIDE")
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_WALK_WINDOW_CLOSE + 1,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_WALK_WINDOW)
            .setSmallIcon(R.drawable.ic_notification_tide)
            .setContentTitle(title)
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setBigContentTitle(title)
                    .setSummaryText("⛔ Walk Window Inactive • High Tide")
            )
            .setColor(Color.rgb(185, 28, 28))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_notification_tide, "View Next Low Tide", pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_WALK_WINDOW_CLOSE, notification)
            setLastWalkNotifyTime(context, System.currentTimeMillis())
        } catch (e: Exception) {
            Log.e(TAG, "Failed to display walk window closed notification", e)
        }
    }

    /**
     * Dispatches a Balasore Weather update or severe advisory notification.
     */
    fun showWeatherUpdateNotification(
        context: Context,
        title: String,
        message: String,
        tempCelsius: Int,
        windSpeedKmh: String = "18 km/h",
        alertLevel: String = "INFO"
    ) {
        if (!isWeatherAlertsEnabled(context)) return
        createNotificationChannels(context)
        if (!hasNotificationPermission(context)) return

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "WEATHER")
            putExtra("weather_update", true)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_WEATHER,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val isSevere = alertLevel == "WARNING" || alertLevel == "URGENT" || alertLevel == "CRITICAL"
        val prefix = if (isSevere) "⚠️" else "🌦️"

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_WEATHER)
            .setSmallIcon(R.drawable.ic_notification_weather)
            .setContentTitle("$prefix $title")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setSummaryText("Balasore Weather • ${tempCelsius}°C • $windSpeedKmh")
            )
            .setColor(if (isSevere) Color.rgb(234, 88, 12) else Color.rgb(2, 132, 199))
            .setPriority(if (isSevere) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_notification_weather, "Open Weather Radar", pendingIntent)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_WEATHER, notification)
            setLastWeatherNotifyTime(context, System.currentTimeMillis())
            Log.d(TAG, "Dispatched Weather notification: $title")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to display weather notification", e)
        }
    }

    // === WorkManager Management ===

    /**
     * Schedules periodic background monitoring for Chandipur High Tide warnings
     * and weather changes using WorkManager.
     */
    fun schedulePeriodicTideAndWeatherChecks(context: Context, intervalMinutes: Long = 15) {
        val constraints = Constraints.Builder().build()

        val periodicWork = PeriodicWorkRequestBuilder<ChandipurTideAndWeatherWorker>(
            repeatInterval = intervalMinutes.coerceAtLeast(15),
            repeatIntervalTimeUnit = TimeUnit.MINUTES
        )
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                5,
                TimeUnit.MINUTES
            )
            .addTag(ChandipurTideAndWeatherWorker.TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            ChandipurTideAndWeatherWorker.WORK_NAME_PERIODIC,
            ExistingPeriodicWorkPolicy.UPDATE,
            periodicWork
        )
        Log.i(TAG, "WorkManager: Scheduled periodic Chandipur Tide & Weather Worker ($intervalMinutes mins interval)")
    }

    /**
     * Enqueues an immediate one-time WorkManager task to evaluate tides and weather.
     */
    fun triggerImmediateTideAndWeatherCheck(context: Context) {
        val oneTimeWork = OneTimeWorkRequestBuilder<ChandipurTideAndWeatherWorker>()
            .addTag(ChandipurTideAndWeatherWorker.TAG)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            ChandipurTideAndWeatherWorker.WORK_NAME_ONE_TIME,
            ExistingWorkPolicy.REPLACE,
            oneTimeWork
        )
        Log.i(TAG, "WorkManager: Enqueued immediate Chandipur Tide & Weather Worker")
    }

    /**
     * Observes the WorkManager execution status for UI reflection.
     */
    fun observeWorkInfo(context: Context): Flow<WorkInfo?> {
        return WorkManager.getInstance(context)
            .getWorkInfosForUniqueWorkFlow(ChandipurTideAndWeatherWorker.WORK_NAME_PERIODIC)
            .map { it.firstOrNull() }
    }

    // === Interactive Test Triggers for Verification ===

    fun testTriggerWalkWindowOpening(context: Context) {
        showWalkWindowAboutToOpen(
            context = context,
            minutesUntilOpen = 25,
            distanceRecededKm = 3.6,
            windowDurationHours = 3.5
        )
    }

    fun testTriggerWalkWindowClosing(context: Context) {
        showWalkWindowAboutToClose(
            context = context,
            minutesRemaining = 15,
            isUrgent = true
        )
    }

    fun testTriggerHighTideWarning(context: Context) {
        showHighTideWarning(
            context = context,
            minutesRemaining = 25,
            tideHeightMeters = 4.25,
            isUrgent = true,
            customTitle = "⚠️ TEST: Chandipur High Tide Warning (Incoming in 25m)",
            customMessage = "Test Alert: Bay of Bengal sea is rushing back across Chandipur mudflats. Safe walk window closing. Evacuate outer seabed immediately!"
        )
    }

    fun testTriggerWeatherUpdate(context: Context) {
        showWeatherUpdateNotification(
            context = context,
            title = "Balasore Afternoon Weather Update",
            message = "Current: 32°C, Humidity 78%. Nor'wester squall line forming over Nilagiri hills. Wind gusts up to 55 km/h expected by evening.",
            tempCelsius = 32,
            windSpeedKmh = "24 km/h",
            alertLevel = "WARNING"
        )
    }
}
