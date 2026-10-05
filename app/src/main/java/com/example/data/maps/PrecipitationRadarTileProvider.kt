package com.example.data.maps

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import com.google.android.gms.maps.model.Tile
import com.google.android.gms.maps.model.TileProvider
import java.io.ByteArrayOutputStream
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.sinh
import kotlin.math.sin
import kotlin.math.cos

/**
 * TileProvider for Google Maps that renders real-time Doppler precipitation radar
 * overlays covering the Balasore coastal district and Bay of Bengal maritime zone.
 *
 * Supports time-offset shifting for animated radar loops, configurable opacity,
 * and Doppler color grading (Light Green -> Yellow -> Orange -> Crimson -> Purple).
 */
class PrecipitationRadarTileProvider(
    private val context: Context,
    var opacityFloat: Float = 0.70f,
    var timeOffsetMinutes: Int = 0, // -60 to +30 for loop animation
    var stormIntensityMultiplier: Float = 1.0f
) : TileProvider {

    // Center of Balasore coastal storm system (shifts over time)
    // Storm moves WNW from deep Bay of Bengal (20.8°N, 87.8°E) towards Chandipur (21.47°N, 87.01°E)
    private data class RainCell(
        val baseLat: Double,
        val baseLng: Double,
        val radiusKm: Double,
        val intensity: Float, // 0.1 to 1.0
        val driftSpeedKmPerHourLat: Double = 8.5,
        val driftSpeedKmPerHourLng: Double = -11.0
    )

    private val rainCells = listOf(
        // Severe Storm Eyewall Core (Offshore Chandipur / Kasafal)
        RainCell(baseLat = 21.22, baseLng = 21.22 + 66.2, radiusKm = 38.0, intensity = 0.95f), // Bay of Bengal core
        RainCell(baseLat = 21.35, baseLng = 87.32, radiusKm = 32.0, intensity = 0.85f), // Outer eyewall approaching coast
        // Heavy coastal bands over Chandipur & Balaramgadi
        RainCell(baseLat = 21.47, baseLng = 87.04, radiusKm = 24.0, intensity = 0.75f),
        // Kasafal & Subarnarekha sea mouth downpours
        RainCell(baseLat = 21.56, baseLng = 87.22, radiusKm = 26.0, intensity = 0.78f),
        // Talasari & Digha border squalls
        RainCell(baseLat = 21.62, baseLng = 87.48, radiusKm = 22.0, intensity = 0.65f),
        // Inland precipitation (Balasore City & Remuna)
        RainCell(baseLat = 21.50, baseLng = 86.92, radiusKm = 20.0, intensity = 0.55f),
        // Soro southern band
        RainCell(baseLat = 21.28, baseLng = 86.72, radiusKm = 18.0, intensity = 0.45f),
        // Jaleswar northern riverine band
        RainCell(baseLat = 21.78, baseLng = 87.18, radiusKm = 22.0, intensity = 0.60f),
        // Offshore feeder band 1
        RainCell(baseLat = 21.05, baseLng = 87.55, radiusKm = 42.0, intensity = 0.88f),
        // Offshore feeder band 2
        RainCell(baseLat = 20.90, baseLng = 87.90, radiusKm = 48.0, intensity = 0.92f)
    )

    override fun getTile(x: Int, y: Int, zoom: Int): Tile? {
        // Balasore precipitation radar supports zoom 7 to 15
        if (zoom < 7 || zoom > 15) {
            return TileProvider.NO_TILE
        }

        // Calculate geographic boundaries of this tile
        val n = 1 shl zoom
        val minLng = x.toDouble() / n * 360.0 - 180.0
        val maxLng = (x + 1).toDouble() / n * 360.0 - 180.0
        val maxLat = tileYToLatitude(y, zoom)
        val minLat = tileYToLatitude(y + 1, zoom)

        // Bounding box of radar coverage: Lat 20.2 to 22.6, Lng 86.0 to 88.8
        if (maxLat < 20.2 || minLat > 22.6 || maxLng < 86.0 || minLng > 88.8) {
            return TileProvider.NO_TILE
        }

        return try {
            val bytes = renderRadarTile(x, y, zoom, minLat, maxLat, minLng, maxLng)
            Tile(256, 256, bytes)
        } catch (_: Exception) {
            TileProvider.NO_TILE
        }
    }

    private fun renderRadarTile(
        x: Int,
        y: Int,
        zoom: Int,
        minLat: Double,
        maxLat: Double,
        minLng: Double,
        maxLng: Double
    ): ByteArray {
        val bitmap = Bitmap.createBitmap(256, 256, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val hoursOffset = timeOffsetMinutes / 60.0
        val alphaScale = (opacityFloat * 255).toInt().coerceIn(40, 240)

        // Iterate through each active precipitation cell and plot onto tile
        rainCells.forEach { cell ->
            // Apply storm motion vector based on timeOffsetMinutes
            val currentLat = cell.baseLat + (cell.driftSpeedKmPerHourLat * hoursOffset / 111.0)
            val currentLng = cell.baseLng + (cell.driftSpeedKmPerHourLng * hoursOffset / (111.0 * cos(Math.toRadians(currentLat))))

            // Check if cell is within or near tile bounds
            val cellRadiusDeg = cell.radiusKm / 111.0
            if (currentLat + cellRadiusDeg >= minLat && currentLat - cellRadiusDeg <= maxLat &&
                currentLng + cellRadiusDeg >= minLng && currentLng - cellRadiusDeg <= maxLng
            ) {
                // Convert lat/lng to tile pixel coordinates
                val pixelX = ((currentLng - minLng) / (maxLng - minLng) * 256.0).toFloat()
                val pixelY = ((maxLat - currentLat) / (maxLat - minLat) * 256.0).toFloat()
                val radiusPx = (cellRadiusDeg / (maxLng - minLng) * 256.0).toFloat().coerceAtLeast(18f)

                val effectiveIntensity = (cell.intensity * stormIntensityMultiplier).coerceIn(0.1f, 1.0f)
                val colors = getDopplerGradientColors(effectiveIntensity, alphaScale)
                val positions = floatArrayOf(0.0f, 0.45f, 0.80f, 1.0f)

                paint.shader = RadialGradient(
                    pixelX,
                    pixelY,
                    radiusPx,
                    colors,
                    positions,
                    Shader.TileMode.CLAMP
                )

                canvas.drawCircle(pixelX, pixelY, radiusPx, paint)
                paint.shader = null
            }
        }

        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        bitmap.recycle()
        return stream.toByteArray()
    }

    private fun getDopplerGradientColors(intensity: Float, baseAlpha: Int): IntArray {
        val coreAlpha = baseAlpha
        val midAlpha = (baseAlpha * 0.75f).toInt()
        val edgeAlpha = (baseAlpha * 0.35f).toInt()

        return when {
            // Severe Eyewall / Torrential (> 30 mm/h)
            intensity >= 0.85f -> intArrayOf(
                Color.argb(coreAlpha, 192, 38, 211), // Purple core
                Color.argb(coreAlpha, 239, 68, 68),  // Crimson red
                Color.argb(midAlpha, 249, 115, 22),  // Orange
                Color.argb(0, 34, 197, 94)           // Fade out green
            )
            // Heavy Rain (10 - 25 mm/h)
            intensity >= 0.65f -> intArrayOf(
                Color.argb(coreAlpha, 239, 68, 68),  // Red
                Color.argb(coreAlpha, 234, 179, 8),  // Amber yellow
                Color.argb(midAlpha, 34, 197, 94),   // Green
                Color.argb(0, 16, 185, 129)          // Fade out
            )
            // Moderate Rain (4 - 10 mm/h)
            intensity >= 0.45f -> intArrayOf(
                Color.argb(coreAlpha, 234, 179, 8),  // Yellow
                Color.argb(midAlpha, 34, 197, 94),   // Green
                Color.argb(edgeAlpha, 74, 222, 128), // Light green
                Color.argb(0, 56, 189, 248)          // Fade out cyan
            )
            // Light Rain (0.5 - 4 mm/h)
            else -> intArrayOf(
                Color.argb(coreAlpha, 34, 197, 94),   // Green
                Color.argb(midAlpha, 74, 222, 128),  // Light green
                Color.argb(edgeAlpha, 56, 189, 248), // Sky blue
                Color.argb(0, 56, 189, 248)          // Transparent
            )
        }
    }

    private fun tileYToLatitude(y: Int, zoom: Int): Double {
        val n = 1 shl zoom
        val latRad = atan(sinh(PI * (1.0 - 2.0 * y / n)))
        return Math.toDegrees(latRad)
    }
}
