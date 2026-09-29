package com.example.util

import android.content.Context
import android.content.Intent
import com.example.data.model.BalasoreWeatherAlert
import com.example.data.model.NewsArticle
import com.example.data.remote.EmergencyAlertDto

/**
 * Utility helper to invoke Android's native system ShareSheet (ACTION_SEND + Intent.createChooser).
 * Formats rich messages for sharing local news and emergency weather/civic alerts with others.
 */
object ShareHelper {

    /**
     * Builds formatted share text for a news article.
     */
    fun buildNewsShareText(
        title: String,
        snippet: String,
        source: String,
        category: String,
        odiaTitle: String = ""
    ): Pair<String, String> {
        val subject = "Balasore News: $title"
        val builder = StringBuilder()
        builder.append("📰 ").append(title).append("\n")
        if (odiaTitle.isNotBlank() && odiaTitle != title) {
            builder.append("🇮🇳 ").append(odiaTitle).append("\n")
        }
        builder.append("\n")
        if (snippet.isNotBlank()) {
            builder.append(snippet).append("\n\n")
        }
        builder.append("Category: ").append(category).append("\n")
        builder.append("Source: ").append(source).append("\n\n")
        builder.append("Shared via Balasore 360 App - Hyperlocal News & Alerts")
        return Pair(subject, builder.toString().trim())
    }

    /**
     * Share a local Balasore news article via Android's ShareSheet.
     */
    fun shareNewsArticle(
        context: Context,
        title: String,
        snippet: String,
        source: String,
        category: String,
        odiaTitle: String = ""
    ) {
        val (subject, text) = buildNewsShareText(title, snippet, source, category, odiaTitle)
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(sendIntent, "Share Balasore News").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    /**
     * Share a NewsArticle model object via Android's ShareSheet.
     */
    fun shareNews(context: Context, article: NewsArticle, isOdia: Boolean = false) {
        val title = if (isOdia && article.odiaTitle.isNotBlank()) article.odiaTitle else article.title
        val snippet = if (isOdia && article.odiaSnippet.isNotBlank()) article.odiaSnippet else article.snippet
        shareNewsArticle(
            context = context,
            title = title,
            snippet = snippet,
            source = article.source,
            category = article.category,
            odiaTitle = if (!isOdia) article.odiaTitle else ""
        )
    }

    /**
     * Builds formatted share text for an emergency alert.
     */
    fun buildEmergencyAlertText(alert: EmergencyAlertDto): Pair<String, String> {
        val isCyclone = alert.type.contains("CYCLONE", ignoreCase = true)
        val alertTypeLabel = if (isCyclone) "LIVE CYCLONE WARNING" else "RIVER SPIKE ALERT"
        val subject = "🚨 BALASORE EMERGENCY ALERT: ${alert.title}"

        val builder = StringBuilder()
        builder.append("🚨 BALASORE EMERGENCY ALERT 🚨\n")
        builder.append("━━━━━━━━━━━━━━━━━━━━━━━━━\n")
        builder.append("⚠️ ").append(alert.title).append("\n")
        if (alert.odiaTitle.isNotBlank()) {
            builder.append("🇮🇳 ").append(alert.odiaTitle).append("\n")
        }
        builder.append("Status: ").append(alertTypeLabel).append("\n\n")

        if (alert.summary.isNotBlank()) {
            builder.append(alert.summary).append("\n\n")
        }

        if (alert.windSpeedKmph != null) {
            builder.append("💨 Wind Speed: ").append(alert.windSpeedKmph).append(" km/h gusts\n")
        }
        if (alert.waterLevelMeters != null && alert.dangerLevelMeters != null) {
            builder.append("🌊 Water Level: ").append(alert.waterLevelMeters).append("m (Danger: ").append(alert.dangerLevelMeters).append("m)\n")
        }

        if (alert.actionRequired.isNotBlank()) {
            builder.append("\n🛑 Required Action:\n").append(alert.actionRequired).append("\n")
        }

        if (alert.emergencyHelpline.isNotBlank()) {
            builder.append("\n📞 Control Room Helpline: ").append(alert.emergencyHelpline).append("\n")
        }

        builder.append("\nShared via Balasore 360 App - Stay safe and alert your community!")
        return Pair(subject, builder.toString().trim())
    }

    /**
     * Share a live emergency alert (e.g., Cyclone Warning, River Level Spike) via Android's ShareSheet.
     */
    fun shareEmergencyAlert(
        context: Context,
        alert: EmergencyAlertDto
    ) {
        val (subject, text) = buildEmergencyAlertText(alert)
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(sendIntent, "Share Emergency Alert").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }

    /**
     * Builds formatted share text for a time-sensitive weather alert.
     */
    fun buildWeatherAlertText(
        alert: BalasoreWeatherAlert,
        validityRemainingText: String = ""
    ): Pair<String, String> {
        val subject = "⚠️ Weather Alert: ${alert.title} - Balasore"
        val builder = StringBuilder()
        builder.append("⚠️ BALASORE WEATHER ALERT\n")
        builder.append("━━━━━━━━━━━━━━━━━━━━━━━━━\n")
        builder.append(alert.category.emoji).append(" ").append(alert.title).append("\n")
        if (alert.odiaTitle.isNotBlank()) {
            builder.append(alert.odiaTitle).append("\n")
        }
        builder.append("Severity: ").append(alert.severity.levelName).append("\n")
        if (validityRemainingText.isNotBlank()) {
            builder.append("Validity: ").append(validityRemainingText).append("\n")
        }
        builder.append("\n").append(alert.summary).append("\n\n")

        if (alert.affectedZones.isNotEmpty()) {
            builder.append("📍 Affected Zones: ")
                .append(alert.affectedZones.joinToString(", ") { it.zoneName })
                .append("\n\n")
        }

        if (alert.actionableInstructions.isNotEmpty()) {
            builder.append("🛡️ Safety Guidelines:\n")
            alert.actionableInstructions.take(3).forEachIndexed { i, step ->
                builder.append("${i + 1}. ").append(step).append("\n")
            }
            builder.append("\n")
        }

        if (alert.emergencyContact.isNotBlank()) {
            builder.append("📞 Helpline: ").append(alert.emergencyContact).append("\n")
        }
        builder.append("Authority: ").append(alert.issuingAuthority).append("\n\n")
        builder.append("Shared via Balasore 360 App")
        return Pair(subject, builder.toString().trim())
    }

    /**
     * Share a time-sensitive weather alert via Android's ShareSheet.
     */
    fun shareWeatherAlert(
        context: Context,
        alert: BalasoreWeatherAlert,
        validityRemainingText: String = ""
    ) {
        val (subject, text) = buildWeatherAlertText(alert, validityRemainingText)
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, subject)
            putExtra(Intent.EXTRA_TEXT, text)
        }
        val chooser = Intent.createChooser(sendIntent, "Share Weather Alert").apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        context.startActivity(chooser)
    }
}
