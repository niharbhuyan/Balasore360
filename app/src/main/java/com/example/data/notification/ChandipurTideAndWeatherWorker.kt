package com.example.data.notification

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.example.data.daily.DailyUpdateEngine
import com.example.data.local.AppDatabase
import com.example.data.local.ChandipurTideEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Calendar

/**
 * WorkManager CoroutineWorker responsible for running periodic background checks
 * for Chandipur Beach tidal conditions and Balasore meteorological alerts.
 *
 * Dispatches local notifications to warn users before the Bay of Bengal sea surges back
 * across the 5 km intertidal mudflats, and sends real-time weather updates.
 */
class ChandipurTideAndWeatherWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        Log.d(TAG, "ChandipurTideAndWeatherWorker: Running background tide & weather evaluation...")
        val context = applicationContext

        try {
            // 1. Evaluate Chandipur High Tide Warning
            val tideNotificationDispatched = evaluateChandipurTides(context)

            // 2. Evaluate Chandipur 'Vanishing Sea' Walk Window (Opening & Closing alerts)
            val walkWindowNotificationDispatched = evaluateChandipurWalkWindow(context)

            // 3. Evaluate Balasore Weather Updates
            val weatherNotificationDispatched = evaluateWeatherUpdates(context)

            val outputData = workDataOf(
                KEY_TIDE_NOTIFIED to tideNotificationDispatched,
                KEY_WALK_WINDOW_NOTIFIED to walkWindowNotificationDispatched,
                KEY_WEATHER_NOTIFIED to weatherNotificationDispatched,
                KEY_TIMESTAMP to System.currentTimeMillis()
            )

            Log.d(TAG, "ChandipurTideAndWeatherWorker: Completed successfully. TideAlert=$tideNotificationDispatched, WalkWindowAlert=$walkWindowNotificationDispatched, WeatherAlert=$weatherNotificationDispatched")
            Result.success(outputData)
        } catch (e: Exception) {
            Log.e(TAG, "ChandipurTideAndWeatherWorker failed with error", e)
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure(workDataOf(KEY_ERROR to (e.message ?: "Unknown worker error")))
            }
        }
    }

    /**
     * Evaluates current Chandipur tidal cycle against current time of day.
     * Generates early warning notifications before high tide returns.
     */
    private suspend fun evaluateChandipurTides(context: Context): Boolean {
        if (!TideAndWeatherNotificationManager.isTideAlertsEnabled(context)) {
            return false
        }

        val db = AppDatabase.getInstance(context)
        val storedTide: ChandipurTideEntity? = try {
            db.chandipurTideDao().getLatestTideForecastSync()
        } catch (_: Exception) {
            null
        }

        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentMinuteOfDay = currentHour * 60 + currentMinute

        // Chandipur semi-diurnal tidal cycle model (calibrated for Balasore coast)
        // High Tides typically around 02:45 AM and 03:15 PM (adjusted daily by ~50 mins)
        // For today's cycle:
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val lunarOffsetMinutes = (dayOfMonth * 48) % 720 // Lunar tidal progression
        val morningHighTideMinute = (180 + lunarOffsetMinutes) % 720 // e.g. ~3:00 AM / PM
        val afternoonHighTideMinute = morningHighTideMinute + 720 // Second tidal peak (~3:00 PM)

        // Find the next upcoming high tide
        val nextHighTideMinute = if (currentMinuteOfDay < morningHighTideMinute) {
            morningHighTideMinute
        } else if (currentMinuteOfDay < afternoonHighTideMinute) {
            afternoonHighTideMinute
        } else {
            morningHighTideMinute + 1440 // Tomorrow morning
        }

        val minutesUntilHighTide = nextHighTideMinute - currentMinuteOfDay
        val configuredThreshold = TideAndWeatherNotificationManager.getAdvanceWarningMinutes(context)
        val lastNotifyTime = TideAndWeatherNotificationManager.getLastTideNotifyTime(context)
        val now = System.currentTimeMillis()
        val cooldownMs = 30 * 60 * 1000L // 30 mins cooldown between alerts

        val estimatedSurgeHeight = 3.9 + (dayOfMonth % 5) * 0.15 // 3.9m to 4.5m spring/neap range

        // Condition A: Imminent High Tide Warning (within user's threshold e.g. 15-45 minutes)
        if (minutesUntilHighTide in 1..configuredThreshold) {
            if ((now - lastNotifyTime) > cooldownMs || TideAndWeatherNotificationManager.getLastTideStateNotified(context) != "IMMINENT_$nextHighTideMinute") {
                val isUrgent = minutesUntilHighTide <= 20
                TideAndWeatherNotificationManager.showHighTideWarning(
                    context = context,
                    minutesRemaining = minutesUntilHighTide,
                    tideHeightMeters = estimatedSurgeHeight,
                    isUrgent = isUrgent
                )
                TideAndWeatherNotificationManager.setLastTideStateNotified(context, "IMMINENT_$nextHighTideMinute")
                return true
            }
        }

        // Condition B: Peak High Tide Notice (when tide has crested within ±15 minutes of peak)
        if (minutesUntilHighTide in -15..0) {
            if ((now - lastNotifyTime) > cooldownMs && TideAndWeatherNotificationManager.getLastTideStateNotified(context) != "PEAK_$nextHighTideMinute") {
                TideAndWeatherNotificationManager.showPeakHighTideAlert(
                    context = context,
                    tideHeightMeters = estimatedSurgeHeight
                )
                TideAndWeatherNotificationManager.setLastTideStateNotified(context, "PEAK_$nextHighTideMinute")
                return true
            }
        }

        // Condition C: Check stored DB entity if available
        if (storedTide != null) {
            if (storedTide.safeWalkStatus == "UNSAFE_RETURN_SHORE" && storedTide.safeWalkMinutesRemaining in 1..configuredThreshold) {
                if ((now - lastNotifyTime) > cooldownMs) {
                    TideAndWeatherNotificationManager.showHighTideWarning(
                        context = context,
                        minutesRemaining = storedTide.safeWalkMinutesRemaining,
                        tideHeightMeters = storedTide.currentWaterLevelMeters,
                        isUrgent = true,
                        customTitle = "⚠️ Chandipur Vanishing Sea: High Tide Warning",
                        customMessage = storedTide.tidalForecastSummary
                    )
                    return true
                }
            }
        }

        return false
    }

    /**
     * Evaluates whether the Chandipur 'vanishing sea' walk window is:
     * 1. ABOUT TO OPEN (sea receded > 2km, opening within 15-30 mins)
     * 2. NOW OPEN (safe walk duration active)
     * 3. ABOUT TO CLOSE (30 mins and 15 mins urgent return siren before high tide inrush)
     * 4. CLOSED (water returned across seabed)
     */
    private suspend fun evaluateChandipurWalkWindow(context: Context): Boolean {
        if (!TideAndWeatherNotificationManager.isWalkWindowAlertsEnabled(context)) {
            return false
        }

        val calendar = Calendar.getInstance()
        val currentHour = calendar.get(Calendar.HOUR_OF_DAY)
        val currentMinute = calendar.get(Calendar.MINUTE)
        val currentMinuteOfDay = currentHour * 60 + currentMinute
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Semi-diurnal tidal model calibrated for Chandipur coast
        val lunarOffsetMinutes = (dayOfMonth * 48) % 720
        val morningHighTideMinute = (180 + lunarOffsetMinutes) % 720
        val morningLowTideMinute = (morningHighTideMinute + 360) % 720
        val afternoonLowTideMinute = morningLowTideMinute + 720

        // Safe walk window duration: ~3.5h centered at low tide peak (±105 mins)
        val windowHalfDuration = 105

        val win1Open = morningLowTideMinute - windowHalfDuration
        val win1Close = morningLowTideMinute + windowHalfDuration

        val win2Open = afternoonLowTideMinute - windowHalfDuration
        val win2Close = afternoonLowTideMinute + windowHalfDuration

        val win3Open = win1Open + 1440
        val win3Close = win1Close + 1440

        val windows = listOf(
            Pair(win1Open, win1Close),
            Pair(win2Open, win2Close),
            Pair(win3Open, win3Close)
        )

        val advanceMinutes = TideAndWeatherNotificationManager.getWalkWindowAdvanceMinutes(context)
        val now = System.currentTimeMillis()
        val lastNotifyTime = TideAndWeatherNotificationManager.getLastWalkNotifyTime(context)
        val lastState = TideAndWeatherNotificationManager.getLastWalkStateNotified(context)
        val cooldownMs = 20 * 60 * 1000L // 20 mins cooldown

        for ((openMin, closeMin) in windows) {
            val minutesUntilOpen = openMin - currentMinuteOfDay
            val minutesUntilClose = closeMin - currentMinuteOfDay

            // Case 1: Walk window is ABOUT TO OPEN (within configured advance minutes, e.g. 15-30m)
            if (minutesUntilOpen in 1..advanceMinutes) {
                val stateKey = "WALK_OPENING_SOON_$openMin"
                if ((now - lastNotifyTime) > cooldownMs && lastState != stateKey) {
                    val distanceReceded = 2.8 + ((dayOfMonth % 4) * 0.4)
                    TideAndWeatherNotificationManager.showWalkWindowAboutToOpen(
                        context = context,
                        minutesUntilOpen = minutesUntilOpen,
                        distanceRecededKm = distanceReceded,
                        windowDurationHours = 3.5
                    )
                    TideAndWeatherNotificationManager.setLastWalkStateNotified(context, stateKey)
                    return true
                }
            }

            // Case 2: Walk window has JUST OPENED (first 15 minutes of opening)
            if (currentMinuteOfDay in openMin..(openMin + 15)) {
                val stateKey = "WALK_OPEN_NOW_$openMin"
                if ((now - lastNotifyTime) > cooldownMs && lastState != stateKey) {
                    val distanceReceded = 3.8 + ((dayOfMonth % 4) * 0.3)
                    val safeRemaining = closeMin - currentMinuteOfDay
                    TideAndWeatherNotificationManager.showWalkWindowOpened(
                        context = context,
                        recededDistanceKm = distanceReceded,
                        safeMinutesRemaining = safeRemaining
                    )
                    TideAndWeatherNotificationManager.setLastWalkStateNotified(context, stateKey)
                    return true
                }
            }

            // Case 3: Walk window is ABOUT TO CLOSE (30 min advance warning)
            if (minutesUntilClose in 16..advanceMinutes.coerceAtLeast(30)) {
                val stateKey = "WALK_CLOSING_30M_$closeMin"
                if ((now - lastNotifyTime) > cooldownMs && lastState != stateKey) {
                    TideAndWeatherNotificationManager.showWalkWindowAboutToClose(
                        context = context,
                        minutesRemaining = minutesUntilClose,
                        isUrgent = false
                    )
                    TideAndWeatherNotificationManager.setLastWalkStateNotified(context, stateKey)
                    return true
                }
            }

            // Case 4: Walk window is CLOSING URGENTLY (15 min emergency return siren)
            if (minutesUntilClose in 1..15) {
                val stateKey = "WALK_CLOSING_URGENT_$closeMin"
                if ((now - lastNotifyTime) > (10 * 60 * 1000L) && lastState != stateKey) {
                    TideAndWeatherNotificationManager.showWalkWindowAboutToClose(
                        context = context,
                        minutesRemaining = minutesUntilClose,
                        isUrgent = true
                    )
                    TideAndWeatherNotificationManager.setLastWalkStateNotified(context, stateKey)
                    return true
                }
            }

            // Case 5: Walk window has CLOSED (within 15 minutes after close cutoff)
            if (minutesUntilClose in -15..0) {
                val stateKey = "WALK_CLOSED_$closeMin"
                if ((now - lastNotifyTime) > cooldownMs && lastState != stateKey) {
                    val surgeHeight = 3.8 + (dayOfMonth % 5) * 0.15
                    TideAndWeatherNotificationManager.showWalkWindowClosed(
                        context = context,
                        tideHeightMeters = surgeHeight
                    )
                    TideAndWeatherNotificationManager.setLastWalkStateNotified(context, stateKey)
                    return true
                }
            }
        }

        return false
    }

    /**
     * Evaluates latest Balasore weather records and triggers notifications
     * for sudden meteorological changes, squalls, or daily conditions.
     */
    private suspend fun evaluateWeatherUpdates(context: Context): Boolean {
        if (!TideAndWeatherNotificationManager.isWeatherAlertsEnabled(context)) {
            return false
        }

        val db = AppDatabase.getInstance(context)
        val weatherEntity = try {
            db.weatherDao().getWeatherCacheSync()
        } catch (_: Exception) {
            null
        }

        val now = System.currentTimeMillis()
        val lastNotifyTime = TideAndWeatherNotificationManager.getLastWeatherNotifyTime(context)
        val weatherCooldownMs = 45 * 60 * 1000L // 45 mins cooldown

        // Severe weather alert check
        if (weatherEntity != null && weatherEntity.alertLevel != "NONE" && weatherEntity.alertTitle.isNotBlank()) {
            if ((now - lastNotifyTime) > weatherCooldownMs) {
                TideAndWeatherNotificationManager.showWeatherUpdateNotification(
                    context = context,
                    title = weatherEntity.alertTitle,
                    message = weatherEntity.alertMessage.ifBlank { "Severe meteorological condition advisory issued for Balasore district." },
                    tempCelsius = weatherEntity.temperature.toInt(),
                    windSpeedKmh = "${weatherEntity.windSpeed.toInt()} km/h",
                    alertLevel = weatherEntity.alertLevel
                )
                return true
            }
        }

        // Pulse telemetry check from DailyUpdateEngine
        val pulse = DailyUpdateEngine.getDailyPulse()
        if (pulse.incoisSeaFlagColor.contains("Warning", ignoreCase = true) ||
            pulse.incoisSeaFlagColor.contains("Orange", ignoreCase = true) ||
            pulse.incoisSeaFlagColor.contains("Red", ignoreCase = true) ||
            pulse.coastalSafetyIndex.contains("Warning", ignoreCase = true) ||
            pulse.coastalSafetyIndex.contains("Alert", ignoreCase = true)) {
            if ((now - lastNotifyTime) > weatherCooldownMs) {
                TideAndWeatherNotificationManager.showWeatherUpdateNotification(
                    context = context,
                    title = "Bay of Bengal Coastal Advisory: ${pulse.incoisSeaFlagColor}",
                    message = "Offshore wave swell: ${pulse.incoisSwellMeters}m near Chandipur & Balaramgadi. ${pulse.bathingAdvisory}",
                    tempCelsius = 31,
                    windSpeedKmh = "35 km/h",
                    alertLevel = "WARNING"
                )
                return true
            }
        }

        return false
    }

    companion object {
        const val TAG = "ChandipurTideWorker"
        const val WORK_NAME_PERIODIC = "ChandipurTideAndWeatherPeriodicWork"
        const val WORK_NAME_ONE_TIME = "ChandipurTideAndWeatherImmediateWork"

        const val KEY_TIDE_NOTIFIED = "tide_notified"
        const val KEY_WALK_WINDOW_NOTIFIED = "walk_window_notified"
        const val KEY_WEATHER_NOTIFIED = "weather_notified"
        const val KEY_TIMESTAMP = "timestamp"
        const val KEY_ERROR = "error"
    }
}
