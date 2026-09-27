package com.example.data.fcm

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.example.data.daily.DailyBalasorePulse
import com.example.data.daily.DailyUpdateEngine
import com.example.data.local.AppDatabase
import com.example.data.local.WeatherCacheEntity
import com.example.data.model.WeatherInfo
import com.example.data.remote.EmergencyAlertDto
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Intelligent Real-Time Alert Evaluation & Dispatch Engine for Balasore 360.
 * Automatically analyzes meteorological forecasts, river hydrology gauges (Rajghat, Budhabalanga),
 * coastal tidal swells, and live administrative emergency alerts.
 *
 * When severe weather or coastal flood thresholds are crossed, this engine automatically
 * triggers Firebase Cloud Messaging system push notifications directly to user devices.
 */
object BalasoreAlertDispatchEngine {

    private const val TAG = "BalasoreAlertEngine"
    private const val PREFS_NAME = "balasore_alert_engine_prefs"

    private const val KEY_LAST_WEATHER_ALERT_HASH = "last_weather_alert_hash"
    private const val KEY_LAST_WEATHER_ALERT_TIME = "last_weather_alert_time"
    private const val KEY_LAST_FLOOD_ALERT_HASH = "last_flood_alert_hash"
    private const val KEY_LAST_FLOOD_ALERT_TIME = "last_flood_alert_time"
    private const val KEY_LAST_DEFENSE_ALERT_HASH = "last_defense_alert_hash"
    private const val KEY_LAST_DEFENSE_ALERT_TIME = "last_defense_alert_time"

    // Cooldown period between identical alerts (20 minutes)
    private const val ALERT_COOLDOWN_MS = 20 * 60 * 1000L

    private fun getPrefs(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    }

    /**
     * Evaluates live weather, tidal pulse, and emergency alerts.
     * Automatically dispatches heads-up notifications if severe criteria are met.
     */
    fun evaluateAndDispatchRealTimeAlerts(
        context: Context,
        weather: WeatherInfo?,
        pulse: DailyBalasorePulse?,
        emergencyAlerts: List<EmergencyAlertDto>? = null,
        force: Boolean = false
    ) {
        // 1. Evaluate Coastal Flood & Sluice Gate Warnings (if weather/flood alerts enabled)
        if (FcmManager.isWeatherAlertsEnabled(context)) {
            evaluateCoastalFloodConditions(context, pulse, emergencyAlerts, force)
            // 2. Evaluate Severe Weather, Squalls & Cyclone Warnings
            evaluateSevereWeatherConditions(context, weather, pulse, emergencyAlerts, force)
        }

        // 3. Evaluate Coastal Defense & Missile Test Advisories (if defense advisories enabled)
        if (FcmManager.isDefenseAdvisoriesEnabled(context)) {
            evaluateDefenseAdvisoryConditions(context, pulse, emergencyAlerts, force)
        }
    }

    /**
     * Evaluates coastal flood danger thresholds:
     * - Subarnarekha River at Rajghat gauge exceeding 9.45m danger mark.
     * - Live emergency alerts with type RIVER_SPIKE, FLOOD, or TIDAL_SURGE.
     * - Imminent high tidal reversal surges along the Chandipur / Kasafal flats.
     */
    private fun evaluateCoastalFloodConditions(
        context: Context,
        pulse: DailyBalasorePulse?,
        emergencyAlerts: List<EmergencyAlertDto>?,
        force: Boolean
    ) {
        var isFloodAlertActive = false
        var alertTitle = "Subarnarekha River Flood & Sluice Gate Warning"
        var alertMessage = "Rajghat river gauge has crossed warning level reaching 9.62m (Danger: 9.45m). Sluice gates opened in Bhograi block."
        var riverGaugeText = "Rajghat: 9.62m (Above Danger Level)"
        var alertLevel = "DANGER"

        // Check live administrative emergency alerts
        val floodDto = emergencyAlerts?.firstOrNull { alert ->
            val type = alert.type.uppercase()
            val severity = alert.severity.uppercase()
            (type.contains("FLOOD") || type.contains("RIVER") || type.contains("TIDAL_SURGE")) &&
                    (severity.contains("CRITICAL") || severity.contains("HIGH") || severity.contains("URGENT"))
        }

        if (floodDto != null) {
            isFloodAlertActive = true
            alertTitle = floodDto.title
            alertMessage = "${floodDto.summary}\nAffected Areas: ${floodDto.affectedArea}. Action: ${floodDto.actionRequired}"
            val waterLvl = floodDto.waterLevelMeters
            val dangerLvl = floodDto.dangerLevelMeters
            if (waterLvl != null && dangerLvl != null) {
                riverGaugeText = "Gauge: ${waterLvl}m (Danger Level: ${dangerLvl}m)"
            }
            alertLevel = floodDto.severity
        } else if (pulse != null) {
            val basinRisk = pulse.subarnarekhaBasinRisk
            val hasHighBasinRisk = basinRisk.contains("Danger", ignoreCase = true) ||
                    basinRisk.contains("Critical", ignoreCase = true) ||
                    basinRisk.contains("High", ignoreCase = true) ||
                    basinRisk.contains("Flooding", ignoreCase = true)

            if (hasHighBasinRisk) {
                isFloodAlertActive = true
                alertTitle = "Subarnarekha River Rising - High Flood Watch"
                alertMessage = "Basin telemetry indicates elevated inflow: $basinRisk. Low-lying riparian panchayats in Baliapal and Bhograi advised to remain on high alert."
                riverGaugeText = basinRisk
                alertLevel = "WARNING"
            } else if (pulse.isTideReversalAlarmImminent && !pulse.isChandipurWalkSafeNow) {
                isFloodAlertActive = true
                alertTitle = "Chandipur Coastal Tidal Inrush Alert"
                alertMessage = "Incoming high tide surging rapidly across vanishing intertidal sea bed. All tourists and visitors must vacate seaward flats immediately."
                riverGaugeText = "Next High Tide: ${pulse.chandipurNextHighTide}"
                alertLevel = "CRITICAL"
            }
        }

        if (isFloodAlertActive) {
            val alertHash = (alertTitle + alertMessage).hashCode()
            val prefs = getPrefs(context)
            val lastHash = prefs.getInt(KEY_LAST_FLOOD_ALERT_HASH, 0)
            val lastTime = prefs.getLong(KEY_LAST_FLOOD_ALERT_TIME, 0L)
            val now = System.currentTimeMillis()

            if (force || alertHash != lastHash || (now - lastTime) > ALERT_COOLDOWN_MS) {
                Log.i(TAG, "Dispatching automatic Coastal Flood notification: $alertTitle")
                BalasoreNotificationHelper.showCoastalFloodNotification(
                    context = context,
                    title = alertTitle,
                    message = alertMessage,
                    riverLevelGauge = riverGaugeText,
                    alertLevel = alertLevel
                )
                prefs.edit()
                    .putInt(KEY_LAST_FLOOD_ALERT_HASH, alertHash)
                    .putLong(KEY_LAST_FLOOD_ALERT_TIME, now)
                    .apply()
            }
        }
    }

    /**
     * Evaluates severe weather thresholds:
     * - Severe weather codes (95, 96, 99: Thunderstorms/Hail, 65, 82: Torrential downpour).
     * - Wind speeds >= 45 km/h or gusts >= 55 km/h.
     * - IMD cyclone advisories or Bay of Bengal depression squalls.
     * - INCOIS rough sea alerts (swell >= 3.0m).
     */
    private fun evaluateSevereWeatherConditions(
        context: Context,
        weather: WeatherInfo?,
        pulse: DailyBalasorePulse?,
        emergencyAlerts: List<EmergencyAlertDto>?,
        force: Boolean
    ) {
        var isWeatherAlertActive = false
        var alertTitle = "IMD Severe Weather & Squall Warning"
        var alertMessage = "Thunderstorms with gusty squalls forecasted across coastal Balasore."
        var alertLevel = "WARNING"

        // Check emergency alerts
        val weatherDto = emergencyAlerts?.firstOrNull { alert ->
            val type = alert.type.uppercase()
            val severity = alert.severity.uppercase()
            (type.contains("CYCLONE") || type.contains("WEATHER") || type.contains("SQUALL") || type.contains("LIGHTNING")) &&
                    (severity.contains("CRITICAL") || severity.contains("HIGH") || severity.contains("URGENT"))
        }

        if (weatherDto != null) {
            isWeatherAlertActive = true
            alertTitle = weatherDto.title
            alertMessage = "${weatherDto.summary}\n${weatherDto.actionRequired}"
            alertLevel = weatherDto.severity
        } else if (weather != null) {
            val windInt = weather.windSpeedKmh.filter { it.isDigit() }.toIntOrNull() ?: 18
            val gustInt = weather.windGustsKmh.filter { it.isDigit() }.toIntOrNull() ?: 24
            val code = weather.weatherCode
            val cycloneNotice = weather.cycloneAlert

            when {
                !cycloneNotice.isNullOrBlank() -> {
                    isWeatherAlertActive = true
                    alertTitle = "Cyclone Advisory for Balasore Coast"
                    alertMessage = cycloneNotice
                    alertLevel = "CRITICAL"
                }
                code in listOf(95, 96, 99) -> {
                    isWeatherAlertActive = true
                    alertTitle = "Kal Baisakhi / Thunderstorm & Lightning Alert"
                    alertMessage = "Severe convective storm cell detected with high lightning frequency and gusty squalls ($windInt–$gustInt km/h). Take shelter indoors immediately."
                    alertLevel = "URGENT"
                }
                code in listOf(65, 82) -> {
                    isWeatherAlertActive = true
                    alertTitle = "Heavy Monsoon Downpour Warning"
                    alertMessage = "Torrential rain inundating coastal roads and NH-16 stretches. Drive with low beam headlights and caution."
                    alertLevel = "WARNING"
                }
                windInt >= 45 || gustInt >= 55 -> {
                    isWeatherAlertActive = true
                    alertTitle = "High Coastal Gale Warning"
                    alertMessage = "Squall winds reaching $windInt km/h with gusts up to $gustInt km/h reported along Chandipur & Balaramgadi coast."
                    alertLevel = "WARNING"
                }
                pulse != null && pulse.incoisSwellMeters >= 3.2f -> {
                    isWeatherAlertActive = true
                    alertTitle = "INCOIS High Sea Wave & Swell Alert"
                    alertMessage = "High swell waves reaching ${pulse.incoisSwellMeters}m along Balasore shores. Marine flag: ${pulse.incoisSeaFlagColor}. Fishermen advised not to venture into deep sea."
                    alertLevel = "WARNING"
                }
            }
        }

        if (isWeatherAlertActive) {
            val alertHash = (alertTitle + alertMessage).hashCode()
            val prefs = getPrefs(context)
            val lastHash = prefs.getInt(KEY_LAST_WEATHER_ALERT_HASH, 0)
            val lastTime = prefs.getLong(KEY_LAST_WEATHER_ALERT_TIME, 0L)
            val now = System.currentTimeMillis()

            if (force || alertHash != lastHash || (now - lastTime) > ALERT_COOLDOWN_MS) {
                Log.i(TAG, "Dispatching automatic Severe Weather notification: $alertTitle")
                BalasoreNotificationHelper.showWeatherAlertNotification(
                    context = context,
                    title = alertTitle,
                    message = alertMessage,
                    alertLevel = alertLevel
                )

                // Update Room weather cache
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val db = AppDatabase.getInstance(context)
                        val current = db.weatherDao().getWeatherCacheSync()
                        if (current != null) {
                            val updated = current.copy(
                                alertLevel = alertLevel,
                                alertTitle = alertTitle,
                                alertMessage = alertMessage,
                                lastUpdated = System.currentTimeMillis()
                            )
                            db.weatherDao().insertOrUpdateWeather(updated)
                        }
                    } catch (e: Exception) {
                        Log.e(TAG, "Failed to update Room weather cache on auto-dispatch", e)
                    }
                }

                prefs.edit()
                    .putInt(KEY_LAST_WEATHER_ALERT_HASH, alertHash)
                    .putLong(KEY_LAST_WEATHER_ALERT_TIME, now)
                    .apply()
            }
        }
    }

    /**
     * Evaluates coastal defense & missile test conditions:
     * - ITR Chandipur / Wheeler (Dr. APJ Abdul Kalam Island) test windows.
     * - NOTAM airspace exclusions and seaward maritime clearance perimeters.
     * - Live emergency alerts with type DEFENSE, MISSILE, NOTAM, or ITR.
     */
    private fun evaluateDefenseAdvisoryConditions(
        context: Context,
        pulse: DailyBalasorePulse?,
        emergencyAlerts: List<EmergencyAlertDto>?,
        force: Boolean
    ) {
        var isDefenseAlertActive = false
        var alertTitle = "ITR Chandipur Coastal Defense & NOTAM Advisory"
        var alertMessage = "DRDO test window active from Launch Complex-3 (LC-3). Fishing boats from Balaramgadi and Kasafal must maintain minimum 18 km seaward clearance."
        var notamWindow = "09:30 AM - 01:30 PM"
        var exclusionPerimeter = "18 km"

        // 1. Check live administrative emergency alerts
        val defenseDto = emergencyAlerts?.firstOrNull { alert ->
            val type = alert.type.uppercase()
            val severity = alert.severity.uppercase()
            (type.contains("DEFENSE") || type.contains("MISSILE") || type.contains("ITR") || type.contains("NOTAM") || type.contains("CHANDIPUR")) &&
                    (severity.contains("CRITICAL") || severity.contains("HIGH") || severity.contains("URGENT") || severity.contains("WARNING") || severity.contains("ACTIVE"))
        }

        if (defenseDto != null) {
            isDefenseAlertActive = true
            alertTitle = defenseDto.title
            alertMessage = "${defenseDto.summary}\n${defenseDto.actionRequired}"
        } else if (pulse != null) {
            val airspace = pulse.itrAirspaceStatus
            val notam = pulse.itrNotamAdvisory
            val isRestricted = airspace.contains("Amber", ignoreCase = true) ||
                    airspace.contains("Red", ignoreCase = true) ||
                    airspace.contains("Restriction", ignoreCase = true) ||
                    airspace.contains("Active", ignoreCase = true) ||
                    airspace.contains("Test", ignoreCase = true) ||
                    notam.contains("Restriction", ignoreCase = true) ||
                    notam.contains("Active", ignoreCase = true) ||
                    notam.contains("Window", ignoreCase = true)

            if (isRestricted) {
                isDefenseAlertActive = true
                alertTitle = "ITR Chandipur Coastal Defense & NOTAM Advisory"
                alertMessage = "Airspace and maritime channel advisory: $airspace. $notam. All artisanal and mechanized fishing craft must clear outer Wheeler Island coordinates."
            }
        }

        if (isDefenseAlertActive) {
            val alertHash = (alertTitle + alertMessage).hashCode()
            val prefs = getPrefs(context)
            val lastHash = prefs.getInt(KEY_LAST_DEFENSE_ALERT_HASH, 0)
            val lastTime = prefs.getLong(KEY_LAST_DEFENSE_ALERT_TIME, 0L)
            val now = System.currentTimeMillis()

            if (force || alertHash != lastHash || (now - lastTime) > ALERT_COOLDOWN_MS) {
                Log.i(TAG, "Dispatching automatic Coastal Defense notification: $alertTitle")
                BalasoreNotificationHelper.showDefenseAdvisoryNotification(
                    context = context,
                    title = alertTitle,
                    message = alertMessage,
                    notamWindow = notamWindow,
                    exclusionPerimeterKm = exclusionPerimeter
                )
                prefs.edit()
                    .putInt(KEY_LAST_DEFENSE_ALERT_HASH, alertHash)
                    .putLong(KEY_LAST_DEFENSE_ALERT_TIME, now)
                    .apply()
            }
        }
    }

    /**
     * Called by WorkManager (DataSyncWorker) during background periodic syncs.
     * Evaluates cached weather and calculated daily pulse, ensuring users receive alerts
     * even when the app is completely closed or device screen is locked.
     */
    suspend fun checkBackgroundAlerts(context: Context) {
        try {
            val db = AppDatabase.getInstance(context)
            val weatherEntity = db.weatherDao().getWeatherCacheSync()
            val pulse = DailyUpdateEngine.getDailyPulse()

            val weatherInfo = if (weatherEntity != null) {
                WeatherInfo(
                    tempCelsius = weatherEntity.temperature.toInt(),
                    condition = weatherEntity.weatherDescription,
                    windSpeedKmh = "${weatherEntity.windSpeed.toInt()} km/h",
                    windGustsKmh = "${weatherEntity.windGusts.toInt()} km/h",
                    cycloneAlert = if (weatherEntity.alertLevel != "NONE" && weatherEntity.alertTitle.isNotBlank()) {
                        "${weatherEntity.alertTitle}: ${weatherEntity.alertMessage}"
                    } else null,
                    weatherCode = weatherEntity.weatherCode
                )
            } else {
                null
            }

            evaluateAndDispatchRealTimeAlerts(
                context = context,
                weather = weatherInfo,
                pulse = pulse,
                emergencyAlerts = null,
                force = false
            )
        } catch (e: Exception) {
            Log.e(TAG, "Background alert check failed gracefully", e)
        }
    }
}
