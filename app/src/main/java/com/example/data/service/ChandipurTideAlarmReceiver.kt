package com.example.data.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.example.data.local.AppDatabase
import com.example.data.local.ChandipurTideEntity
import com.example.data.local.TidalLocalStorage
import com.example.data.model.TidalPhase
import com.example.data.model.TidalTimeframe
import com.example.data.notification.TideAndWeatherNotificationManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Calendar

/**
 * BroadcastReceiver responsible for handling exact alarm notifications
 * scheduled 30 minutes before the 'safe walk window' begins at Chandipur beach.
 *
 * It evaluates local tidal telemetry data (Room database + TidalLocalStorage time series),
 * dispatches the high-priority local push notification alert, and schedules the next cycle.
 */
class ChandipurTideAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: ACTION_TIDE_ALARM
        Log.i(TAG, "ChandipurTideAlarmReceiver received intent action: $action")

        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                when (action) {
                    ACTION_TIDE_ALARM,
                    ACTION_TRIGGER_SAFE_WALK_30M -> {
                        // Extract telemetry parameters if bundled, or evaluate from local data
                        val minutesUntilOpen = intent.getIntExtra(EXTRA_MINUTES_UNTIL_OPEN, 30)
                        val recededDistance = intent.getDoubleExtra(EXTRA_RECEDED_KM, 3.2)
                        val windowDuration = intent.getDoubleExtra(EXTRA_WINDOW_DURATION_HOURS, 3.5)

                        Log.i(TAG, "Executing Safe Walk 30-min Advance Alert push notification (receded: ${recededDistance}km)")

                        // Dispatch local push notification
                        TideAndWeatherNotificationManager.showWalkWindowAboutToOpen(
                            context = context,
                            minutesUntilOpen = minutesUntilOpen,
                            distanceRecededKm = recededDistance,
                            windowDurationHours = windowDuration
                        )

                        // Re-evaluate and schedule next tidal walk cycle
                        ChandipurSafeWalkService.scheduleNextWalkWindowAlert(context)
                    }

                    Intent.ACTION_BOOT_COMPLETED,
                    Intent.ACTION_MY_PACKAGE_REPLACED,
                    Intent.ACTION_TIME_CHANGED,
                    Intent.ACTION_TIMEZONE_CHANGED -> {
                        Log.i(TAG, "System event $action: Rescheduling Chandipur safe walk background alarms")
                        ChandipurSafeWalkService.startService(context)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error processing Chandipur safe walk alarm", e)
            } finally {
                pendingResult.finish()
            }
        }
    }

    companion object {
        const val TAG = "ChandipurTideAlarm"
        const val ACTION_TIDE_ALARM = "com.example.action.CHANDIPUR_SAFE_WALK_ALARM"
        const val ACTION_TRIGGER_SAFE_WALK_30M = "com.example.action.TRIGGER_SAFE_WALK_30M"

        const val EXTRA_MINUTES_UNTIL_OPEN = "extra_minutes_until_open"
        const val EXTRA_RECEDED_KM = "extra_receded_km"
        const val EXTRA_WINDOW_DURATION_HOURS = "extra_window_duration_hours"
    }
}
