package com.example.data.fcm

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R

object BalasoreNotificationHelper {

    const val CHANNEL_ID_WEATHER = "balasore_weather_alerts"
    const val CHANNEL_NAME_WEATHER = "Urgent Weather & Tide Alerts"

    const val CHANNEL_ID_COASTAL_FLOOD = "balasore_coastal_flood_alerts"
    const val CHANNEL_NAME_COASTAL_FLOOD = "Coastal Flood & Sluice Warnings"

    const val CHANNEL_ID_NEWS = "balasore_breaking_news"
    const val CHANNEL_NAME_NEWS = "Balasore Breaking News"

    const val EXTRA_TARGET_TAB = "target_tab"
    const val EXTRA_ARTICLE_ID = "article_id"

    const val NOTIFICATION_ID_WEATHER_BASE = 2001
    const val NOTIFICATION_ID_FLOOD_BASE = 4001
    const val NOTIFICATION_ID_NEWS_BASE = 3001

    fun createNotificationChannels(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

            // 1. Weather Alerts Channel
            val weatherChannel = NotificationChannel(
                CHANNEL_ID_WEATHER,
                CHANNEL_NAME_WEATHER,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Critical alerts for Bay of Bengal squalls, cyclones, Nor'westers, and Chandipur tidal surges"
                enableLights(true)
                lightColor = Color.RED
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 350, 150, 350)
            }

            // 2. Coastal Flood Channel
            val floodChannel = NotificationChannel(
                CHANNEL_ID_COASTAL_FLOOD,
                CHANNEL_NAME_COASTAL_FLOOD,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Live Subarnarekha and Budhabalanga river danger gauge, sluice gate release, and storm surge warnings"
                enableLights(true)
                lightColor = Color.rgb(0, 150, 255)
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 400, 200, 400)
            }

            // 3. Breaking News Channel
            val newsChannel = NotificationChannel(
                CHANNEL_ID_NEWS,
                CHANNEL_NAME_NEWS,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Breaking local news wire, district magistrate orders, and civic bulletins"
                enableLights(true)
                lightColor = Color.BLUE
                enableVibration(true)
            }

            notificationManager.createNotificationChannel(weatherChannel)
            notificationManager.createNotificationChannel(floodChannel)
            notificationManager.createNotificationChannel(newsChannel)
        }
    }

    fun showWeatherAlertNotification(
        context: Context,
        title: String,
        message: String,
        alertLevel: String = "URGENT"
    ) {
        createNotificationChannels(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "WEATHER")
            putExtra("alert_title", title)
            putExtra("alert_message", message)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_WEATHER_BASE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_WEATHER)
            .setSmallIcon(R.drawable.ic_notification_weather)
            .setContentTitle("⚠️ $title")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setSummaryText("Balasore 360 Weather Warning • $alertLevel")
            )
            .setColor(0xFFD9381E.toInt()) // Alert Crimson
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_notification_weather,
                "Check Tides & Forecast",
                pendingIntent
            )
            .build()

        val notificationId = NOTIFICATION_ID_WEATHER_BASE + (System.currentTimeMillis() % 100).toInt()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun showCoastalFloodNotification(
        context: Context,
        title: String,
        message: String,
        riverLevelGauge: String = "Rajghat: 9.45m",
        alertLevel: String = "DANGER"
    ) {
        createNotificationChannels(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "WEATHER")
            putExtra("flood_alert", true)
            putExtra("alert_title", title)
            putExtra("alert_message", message)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_FLOOD_BASE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_COASTAL_FLOOD)
            .setSmallIcon(R.drawable.ic_notification_weather)
            .setContentTitle("🌊 $title")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("$message\n\nHydro Gauge: $riverLevelGauge\nAction: Evacuate low-lying riverine wards & follow ODRAF advisories.")
                    .setSummaryText("Balasore Coastal Flood Alert • $alertLevel")
            )
            .setColor(0xFF0284C7.toInt()) // Deep Cyan Blue
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setCategory(NotificationCompat.CATEGORY_ALARM)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_notification_weather,
                "Open Flood & Sluice Radar",
                pendingIntent
            )
            .build()

        val notificationId = NOTIFICATION_ID_FLOOD_BASE + (System.currentTimeMillis() % 100).toInt()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun showBreakingNewsNotification(
        context: Context,
        title: String,
        message: String,
        articleId: String? = null
    ) {
        createNotificationChannels(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return
            }
        }

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("target_tab", "NEWS")
            putExtra("article_id", articleId)
            putExtra("news_title", title)
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID_NEWS_BASE,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        val notification = NotificationCompat.Builder(context, CHANNEL_ID_NEWS)
            .setSmallIcon(R.drawable.ic_notification_news)
            .setContentTitle("⚡ $title")
            .setContentText(message)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText(message)
                    .setSummaryText("Balasore 360 Breaking Wire • Nihar Sales")
            )
            .setColor(0xFF1A56DB.toInt()) // Bento Primary Blue
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setCategory(NotificationCompat.CATEGORY_EVENT)
            .setSound(soundUri)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .addAction(
                R.drawable.ic_notification_news,
                "Read Full Bulletin",
                pendingIntent
            )
            .build()

        val notificationId = NOTIFICATION_ID_NEWS_BASE + (System.currentTimeMillis() % 100).toInt()
        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }
}
